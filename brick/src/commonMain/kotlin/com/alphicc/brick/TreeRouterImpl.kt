package com.alphicc.brick

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class TreeRouterImpl(
    override val initialScreen: Screen<*>? = null,
    override val parentRouter: TreeRouter? = null
) : TreeRouter {

    private val keyManager = KeyManager()
    private val _currentCompositionsFlow: MutableStateFlow<Map<String, Screen<*>>> = MutableStateFlow(emptyMap())
    private val _currentOverlayFlow: MutableStateFlow<Screen<*>?> = MutableStateFlow(null)
    private val _currentScreenFlow: MutableStateFlow<Screen<*>?> = MutableStateFlow(null)
    private val _currentChildFlow: MutableStateFlow<List<Screen<*>>> = MutableStateFlow(emptyList())
    private val _isRouterEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private val childRouters: AtomicRef<List<Pair<String, TreeRouter>>> = atomic(emptyList())
    private val tree: AtomicRef<List<Node>> = atomic(emptyList())
    private val currentNode: Node?
        get() = tree.value.lastOrNull()

    override val overlay: StateFlow<Screen<*>?> = _currentOverlayFlow

    override val screen: StateFlow<Screen<*>?> = _currentScreenFlow

    override val childList: StateFlow<List<Screen<*>>> = _currentChildFlow

    override val compositions: StateFlow<Map<String, Screen<*>>> = _currentCompositionsFlow

    override val isRouterEmpty: StateFlow<Boolean> = _isRouterEmpty

    override fun currentScreenKey(): String? = screen.value?.key

    //return "true" if has back navigation variants else false
    override fun onBackClicked() {
        when {
            currentNode?.childScreens()?.isNotEmpty() == true -> backChild()
            currentNode?.parent != null -> backScreen()
            else -> backScreen()
        }
    }

    override fun attachCompositeScreen(screen: Screen<*>) =
        attachCompositeScreenToNode(screen, null)

    override fun <A> attachCompositeScreen(screen: Screen<*>, argument: A) =
        attachCompositeScreenToNode(screen, argument)

    private fun <A> attachCompositeScreenToNode(screen: Screen<*>, argument: A?) {
        val isSuccess = keyManager.add(screen.key)
        if (!isSuccess) return
        val currentNode = currentNode ?: return
        screen.onCreate(argument)
        currentNode.addComposition(screen)
        fetchNode()
    }

    override fun detachCompositeScreen(key: String) {
        val currentNode = currentNode ?: return
        keyManager.remove(key)
        val composition = currentNode.compositions()[key]
        if (composition != null) {
            composition.onDestroy()
            currentNode.removeComposition(key)
            fetchNode()
        }
    }

    override fun backScreen() {
        val nodesTree = tree.value
        nodesTree.lastOrNull()?.let {
            it.childScreens().forEach { childScreen ->
                destroyChildRouters(childScreen.key)
                childScreen.onDestroy()
                keyManager.remove(childScreen.key)
            }
            it.compositions().forEach { entry ->
                destroyChildRouters(entry.key)
                entry.value.onDestroy()
                keyManager.remove(entry.key)
            }
            destroyChildRouters(it.screen.key)
            it.screen.onDestroy()
            keyManager.remove(it.screen.key)
            tree.value = nodesTree.dropLast(1)
            fetchNode()
        }
    }

    override fun getRootRouter(): TreeRouter = parentRouter?.getRootRouter() ?: this

    override fun cleanRouter() {
        cleanGraph()
        removeOverlay()
        fetchNode()
    }

    override fun branch(containerScreenKey: String): TreeRouter {
        val newRouter = TreeRouterImpl(initialScreen, this)
        val childRouters = childRouters.value.toMutableList()
        childRouters.add(containerScreenKey to newRouter)
        this.childRouters.value = childRouters
        return newRouter
    }

    override fun setOverlay(screen: Screen<*>) {
        val rootRouter = getRootRouter()
        if (rootRouter === this) setOverlayNode(screen, null)
    }

    override fun <A> setOverlay(screen: Screen<*>, argument: A) {
        val rootRouter = getRootRouter()
        if (rootRouter === this) setOverlayNode(screen, argument)
    }

    override fun removeOverlay() {
        val rootRouter = getRootRouter()
        if (rootRouter === this) {
            _currentOverlayFlow.value?.let { screen ->
                keyManager.remove(screen.key)
                screen.onDestroy()
                _currentOverlayFlow.value = null
            }
        }
    }

    override suspend fun <A> passArgument(screenKey: String, argument: A) {
        redirectArgument(this, screenKey, argument)
    }

    override fun backToScreen(key: String) {
        val droppedNodes = dropNodeUntilFoundKey(currentNode, key)
        droppedNodes.forEach {
            it.childScreens().forEach { childScreen ->
                destroyChildRouters(childScreen.key)
                childScreen.onDestroy()
                keyManager.remove(childScreen.key)
            }
            it.compositions().forEach { entry ->
                destroyChildRouters(entry.key)
                entry.value.onDestroy()
                keyManager.remove(entry.key)
            }
            destroyChildRouters(it.screen.key)
            it.screen.onDestroy()
            keyManager.remove(it.screen.key)
        }
        fetchNode()
    }

    override fun replaceScreen(screen: Screen<*>) = replaceScreenFromNode(screen, null)

    override fun <A> replaceScreen(screen: Screen<*>, argument: A) = replaceScreenFromNode(screen, argument)

    override fun addScreen(screen: Screen<*>) = addScreenNode(screen, null)

    override fun <A> addScreen(screen: Screen<*>, argument: A) = addScreenNode(screen, argument)

    override fun newRootScreen(screen: Screen<*>) = newRootScreenNode(screen, null)

    override fun <A> newRootScreen(screen: Screen<*>, argument: A) = newRootScreenNode(screen, argument)

    override fun lastChildKey(): String? = childList.value.lastOrNull()?.key

    override fun backChild() {
        currentNode?.run {
            if (childScreens().isNotEmpty()) {
                val screen = childScreens().last()
                dropLastChildScreen()
                keyManager.remove(screen.key)
                destroyChildRouters(screen.key)
                screen.onDestroy()
                fetchNode()
            }
        }
    }

    override fun backToChild(key: String) {
        currentNode?.run {
            val droppedChildList = dropChildUntilFoundKey(this, key)
            droppedChildList.forEach {
                keyManager.remove(it.key)
                destroyChildRouters(it.key)
                it.onDestroy()
            }
            fetchNode()
        }
    }

    override fun replaceChild(screen: Screen<*>) = replaceChildNode(screen, null)

    override fun <A> replaceChild(screen: Screen<*>, argument: A) = replaceChildNode(screen, argument)

    override fun addChild(screen: Screen<*>) = addChildNode(screen, null)

    override fun <A> addChild(screen: Screen<*>, argument: A) = addChildNode(screen, argument)

    override suspend fun <A> redirectArgument(
        from: ArgumentTranslator,
        screenKey: String,
        argument: A
    ) {
        if (parentRouter !== from) {
            parentRouter?.redirectArgument(this, screenKey, argument)
        }
        childRouters.value.forEach { it.second.redirectArgument(this, screenKey, argument) }
        emitArguments(screenKey, argument)
    }

    private suspend fun <A> emitArguments(screenKey: String, argument: A) {
        _currentOverlayFlow.value?.let { overlayScreen ->
            if (overlayScreen.key == screenKey) {
                val dataContainer = DataContainer(argument)
                overlayScreen.channel.emit(dataContainer)
            }
        }

        tree.value.forEach { node ->
            node.childScreens().forEach {
                if (it.key == screenKey) {
                    val dataContainer = DataContainer(argument)
                    it.channel.emit(dataContainer)
                }
            }

            node.compositions().forEach { entry ->
                if (entry.key == screenKey) {
                    val dataContainer = DataContainer(argument)
                    entry.value.channel.emit(dataContainer)
                }
            }

            if (node.screen.key == screenKey) {
                val dataContainer = DataContainer(argument)
                node.screen.channel.emit(dataContainer)
            }
        }
    }

    private fun <A> addChildNode(screen: Screen<*>, argument: A?) {
        currentNode?.run {
            val isSuccess = keyManager.add(screen.key)
            if (!isSuccess) return
            screen.onCreate(argument)
            addChildScreen(screen)
            fetchNode()
        }
    }

    private fun <A> replaceChildNode(screen: Screen<*>, argument: A?) {
        currentNode?.run {
            if (childScreens().isNotEmpty()) {
                val isSuccess = keyManager.replaceKey(childScreens().last().key, screen.key)
                if (!isSuccess) return
                val oldChildScreen = childScreens()[childScreens().size - 1]
                destroyChildRouters(oldChildScreen.key)
                oldChildScreen.onDestroy()
                screen.onCreate(argument)
                replaceLastChildScreen(screen)
                fetchNode()
            }
        }
    }

    private fun <A> newRootScreenNode(screen: Screen<*>, argument: A?) {
        cleanGraph()
        addScreen(screen, argument)
    }

    private fun <A> addScreenNode(screen: Screen<*>, argument: A?) {
        val isSuccess = keyManager.add(screen.key)
        if (!isSuccess) return
        screen.onCreate(argument)
        val node = Node(
            screen = screen,
            parent = currentNode
        )
        tree.value = tree.value
            .toMutableList()
            .apply { add(node) }
        fetchNode()
    }

    private fun <A> replaceScreenFromNode(screen: Screen<*>, argument: A?) {
        currentNode?.let { node ->
            val isSuccess = keyManager.replaceKey(node.screen.key, screen.key)
            if (!isSuccess) return
        }
        currentNode?.screen?.let {
            destroyChildRouters(it.key)
            it.onDestroy()
        }
        screen.onCreate(argument)
        tree.value = tree.value.toMutableList().apply {
            if (this.isNotEmpty()) {
                this[this.lastIndex] = last().copy(screen = screen)
            }
        }
        fetchNode()
    }

    private fun <A> setOverlayNode(screen: Screen<*>, argument: A?) {
        val isSuccess = keyManager.add(screen.key)
        if (!isSuccess) return

        screen.onCreate(argument)
        _currentOverlayFlow.value = screen
    }

    private fun dropChildUntilFoundKey(
        node: Node,
        screenKey: String
    ): List<Screen<*>> {
        val droppedChildList = ArrayList<Screen<*>>()
        node.childScreens().lastOrNull()?.let {
            if (it.key != screenKey) {
                node.dropLastChildScreen()
                droppedChildList.add(it)
                val innerDroppedChildList = dropChildUntilFoundKey(node, screenKey)
                droppedChildList.addAll(innerDroppedChildList)
            } else return@let
        }
        return droppedChildList
    }

    private fun dropNodeUntilFoundKey(node: Node?, screenKey: String): List<Node> {
        val droppedNodes = ArrayList<Node>()
        if (node != null && node.screen.key != screenKey) {
            droppedNodes.add(node)
            val modifiedTree = tree.value.toMutableList().apply {
                removeLastOrNull()
            }
            tree.value = modifiedTree
            val newDroppedNodes = dropNodeUntilFoundKey(node.parent, screenKey)
            droppedNodes.addAll(newDroppedNodes)
        }
        return droppedNodes
    }

    private fun cleanGraph() {
        tree.value.forEach { node ->
            node.childScreens().forEach { childScreen ->
                destroyChildRouters(childScreen.key)
                childScreen.onDestroy()
                keyManager.remove(childScreen.key)
            }
            destroyChildRouters(node.screen.key)
            node.screen.onDestroy()
            keyManager.remove(node.screen.key)
        }
        tree.value = emptyList()

        removeOverlay()
    }

    private fun destroyChildRouters(key: String) {
        val childRouters = childRouters.value.toMutableList()
        childRouters.removeAll {
            if (it.first == key) {
                it.second.cleanRouter()
                true
            } else false
        }
        this.childRouters.value = childRouters
    }

    private fun fetchNode() {
        _isRouterEmpty.value = currentNode == null
        _currentScreenFlow.value = currentNode?.screen
        _currentChildFlow.value = currentNode?.childScreens() ?: emptyList()
        _currentCompositionsFlow.value = currentNode?.compositions() ?: emptyMap()
    }
}