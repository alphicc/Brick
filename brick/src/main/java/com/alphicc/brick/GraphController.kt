package com.alphicc.brick

import kotlinx.coroutines.flow.MutableStateFlow

internal class GraphController(private val graphEventsInterceptor: GraphEventsInterceptor) {

    private val keyManager = KeyManager()
    private val tree: ArrayList<Node> = ArrayList()

    val currentOverlayFlow: MutableStateFlow<Screen<*>?> = MutableStateFlow(null)
    val currentScreenFlow: MutableStateFlow<Screen<*>?> = MutableStateFlow(null)
    val currentChildFlow: MutableStateFlow<List<Screen<*>>> = MutableStateFlow(emptyList())
    val hasBackNavigationVariants: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var currentNode: Node? = null

    fun <A> setOverlay(screen: Screen<*>, argument: A?) {
        val isSuccess = keyManager.add(screen.key)
        if (!isSuccess) return

        val screenLifecycleController = ScreenLifecycleController(screen)
        val createdScreen = screenLifecycleController.onCreate(argument)
        currentOverlayFlow.value = createdScreen
    }

    fun removeOverlay() {
        currentOverlayFlow.value?.let { screen ->
            keyManager.remove(screen.key)
            val screenLifecycleController = ScreenLifecycleController(screen)
            screenLifecycleController.onDestroy()
            currentOverlayFlow.value = null
        }
    }

    suspend fun <A> passArgument(screenKey: String, argument: A) {
        currentOverlayFlow.value?.let { overlayScreen ->
            if (overlayScreen.key == screenKey) {
                val dataContainer = DataContainer(argument)
                overlayScreen.channel.emit(dataContainer)
            }
        }

        tree.forEach { node ->
            node.childScreens.forEach {
                if (it.key == screenKey) {
                    val dataContainer = DataContainer(argument)
                    it.channel.emit(dataContainer)
                }
            }

            if (node.screen.key == screenKey) {
                val dataContainer = DataContainer(argument)
                node.screen.channel.emit(dataContainer)
            }
        }
    }

    fun cleanRouter() {
        cleanGraph()
        removeOverlay()
        updateCurrentNode(null)
    }

    //return "true" if has back navigation variants else false
    fun back() {
        val hasBackNavigationVariants = when {
            currentNode?.childScreens?.isNotEmpty() == true -> {
                backChild()
                currentNode?.childScreens?.isNotEmpty() == true || currentNode?.parent != null
            }
            currentNode?.parent != null -> {
                backScreen()
                currentNode?.parent != null
            }
            else -> false
        }
        this.hasBackNavigationVariants.value = hasBackNavigationVariants
    }

    fun backScreen() {
        tree.removeLastOrNull()?.let {
            it.childScreens.forEach { childScreen ->
                val childScreenLifecycleController = ScreenLifecycleController(childScreen)
                graphEventsInterceptor.onDestroyScreen(childScreen.key)
                childScreenLifecycleController.onDestroy()
                keyManager.remove(childScreen.key)
            }
            val screenLifecycleController = ScreenLifecycleController(it.screen)
            graphEventsInterceptor.onDestroyScreen(it.screen.key)
            screenLifecycleController.onDestroy()
            keyManager.remove(it.screen.key)

            updateCurrentNode(it.parent)
        }
    }

    fun backToScreen(key: String) {
        val droppedNodes = dropNodeUntilFoundKey(currentNode, key)
        droppedNodes.forEach {
            it.childScreens.forEach { childScreen ->
                val childScreenLifecycleController = ScreenLifecycleController(childScreen)
                graphEventsInterceptor.onDestroyScreen(childScreen.key)
                childScreenLifecycleController.onDestroy()
                keyManager.remove(childScreen.key)
            }
            val screenLifecycleController = ScreenLifecycleController(it.screen)
            graphEventsInterceptor.onDestroyScreen(it.screen.key)
            screenLifecycleController.onDestroy()
            keyManager.remove(it.screen.key)
        }

        updateCurrentNode(tree.lastOrNull())
    }

    fun <A> replaceScreen(screen: Screen<*>, argument: A?) {
        currentNode?.let { node ->
            val isSuccess = keyManager.replaceKey(node.screen.key, screen.key)
            if (!isSuccess) return
        }

        currentNode?.screen?.let {
            val oldScreenLifecycleController = ScreenLifecycleController(it)
            graphEventsInterceptor.onDestroyScreen(it.key)
            oldScreenLifecycleController.onDestroy()
        }
        val newScreenLifecycleController = ScreenLifecycleController(screen)
        val newCreatedScreen = newScreenLifecycleController.onCreate(argument)
        currentNode?.screen = newCreatedScreen

        updateCurrentNode(currentNode)
    }

    fun <A> addScreen(screen: Screen<*>, argument: A?) {
        val isSuccess = keyManager.add(screen.key)
        if (!isSuccess) return

        val screenLifecycleController = ScreenLifecycleController(screen)
        val createdScreen = screenLifecycleController.onCreate(argument)
        val node = Node(
            screen = createdScreen,
            childScreens = ArrayList(),
            parent = currentNode
        )
        tree.add(node)

        updateCurrentNode(node)
    }

    fun backChild() {
        currentNode?.run {
            if (childScreens.size >= 1) {
                val screen = childScreens.removeAt(childScreens.size - 1)
                keyManager.remove(screen.key)

                val screenLifecycleController = ScreenLifecycleController(screen)
                graphEventsInterceptor.onDestroyScreen(screen.key)
                screenLifecycleController.onDestroy()

                updateCurrentNode(this)
            }
        }
    }

    fun backToChild(key: String) {
        currentNode?.run {
            val droppedChildList = dropChildUntilFoundKey(childScreens, key)

            droppedChildList.forEach {
                keyManager.remove(it.key)

                val screenLifecycleController = ScreenLifecycleController(it)
                graphEventsInterceptor.onDestroyScreen(it.key)
                screenLifecycleController.onDestroy()
            }

            updateCurrentNode(this)
        }
    }

    fun <A> replaceChild(screen: Screen<*>, argument: A?) {
        currentNode?.run {
            if (childScreens.size >= 1) {
                val isSuccess = keyManager.replaceKey(childScreens.last().key, screen.key)
                if (!isSuccess) return

                val oldChildScreen = childScreens[childScreens.size - 1]
                val oldScreenLifecycleController = ScreenLifecycleController(oldChildScreen)
                graphEventsInterceptor.onDestroyScreen(oldChildScreen.key)
                oldScreenLifecycleController.onDestroy()

                val newScreenLifecycleController = ScreenLifecycleController(screen)
                childScreens[childScreens.size - 1] =
                    newScreenLifecycleController.onCreate(argument)

                updateCurrentNode(this)
            }
        }
    }

    fun <A> addChild(screen: Screen<*>, argument: A?) {
        currentNode?.run {
            val isSuccess = keyManager.add(screen.key)
            if (!isSuccess) return

            val screenLifecycle = ScreenLifecycleController(screen)
            val createdScreen = screenLifecycle.onCreate(argument)
            childScreens.add(createdScreen)

            updateCurrentNode(this)
        }
    }

    fun <A> newRootScreen(screen: Screen<*>, argument: A?) {
        cleanGraph()
        addScreen(screen, argument)
    }

    private fun cleanGraph() {
        tree.forEach { node ->
            node.childScreens.forEach { childScreen ->
                val childScreenLifecycleController = ScreenLifecycleController(childScreen)
                graphEventsInterceptor.onDestroyScreen(childScreen.key)
                childScreenLifecycleController.onDestroy()
                keyManager.remove(childScreen.key)
            }
            val screenLifecycleController = ScreenLifecycleController(node.screen)
            graphEventsInterceptor.onDestroyScreen(node.screen.key)
            screenLifecycleController.onDestroy()
            keyManager.remove(node.screen.key)
        }
        currentNode = null
        tree.clear()

        removeOverlay()
    }

    private fun dropNodeUntilFoundKey(node: Node?, screenKey: String): List<Node> {
        val droppedNodes = ArrayList<Node>()
        if (node != null && node.screen.key != screenKey) {
            droppedNodes.add(node)
            tree.removeLastOrNull()
            val newDroppedNodes = dropNodeUntilFoundKey(node.parent, screenKey)
            droppedNodes.addAll(newDroppedNodes)
        }
        return droppedNodes
    }

    private fun dropChildUntilFoundKey(
        childScreens: ArrayList<Screen<*>>,
        screenKey: String
    ): List<Screen<*>> {
        val droppedChildList = ArrayList<Screen<*>>()
        childScreens.lastOrNull()?.let {
            if (it.key != screenKey) {
                val element = childScreens.removeAt(childScreens.size - 1)
                droppedChildList.add(element)
                val innerDroppedChildList = dropChildUntilFoundKey(childScreens, screenKey)
                droppedChildList.addAll(innerDroppedChildList)
            } else return@let
        }
        return droppedChildList
    }

    private fun updateCurrentNode(node: Node?) {
        currentNode = node
        currentScreenFlow.value = node?.screen
        currentChildFlow.value = node?.childScreens?.toList() ?: emptyList()
        hasBackNavigationVariants.value =
            currentNode?.childScreens?.isNotEmpty() == true || currentNode?.parent != null
    }
}