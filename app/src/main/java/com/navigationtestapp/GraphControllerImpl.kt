package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow

class GraphControllerImpl {

    val currentScreenFlow: MutableStateFlow<Screen<*>?> = MutableStateFlow(null)
    val currentChildFlow: MutableStateFlow<List<Screen<*>>> = MutableStateFlow(emptyList())

    private val keyManager = KeyManager()
    private val tree: ArrayList<Node> = ArrayList()

    private var currentNode: Node? = null

    fun backScreen() {
        currentNode?.isActive = false

        updateCurrentNode(currentNode?.parent)
        val droppedNodes = cleanGraph(tree)
        onDestroyNodes(droppedNodes)
    }

    fun backToScreen(key: String) {
        dropUntil(currentNode, key)

        updateCurrentNode(currentNode?.parent)
        val droppedNodes = cleanGraph(tree)
        onDestroyNodes(droppedNodes)
    }

    fun replaceScreen(screen: Screen<*>) {
        currentNode?.let { node -> keyManager.remove(node.screen.key) }
        keyManager.add(screen.key)

        val oldScreen = currentNode?.screen
        currentNode?.screen = screen

        currentNode?.run {
            screen.onCreate?.invoke(screen.channel, router)
            val dependency = screen.onCreate?.invoke(screen.channel, router)
            val dependencyProvider = DataProvider(dependency)
            screen.dependency = dependencyProvider
        }
        updateCurrentNode(currentNode)
        oldScreen?.onDestroy?.invoke()
    }

    fun addScreen(screen: Screen<*>, treeRouter: TreeRouter) {
        keyManager.add(screen.key)

        val node = Node(
            isActive = true,
            screen = screen,
            childScreens = ArrayList(),
            neighbors = ArrayList(),
            parent = currentNode,
            router = treeRouter
        )
        tree.add(node)

        val dependency = node.screen.onCreate?.invoke(node.screen.channel, node.router)
        val dependencyProvider = DataProvider(dependency)
        node.screen.dependency = dependencyProvider
        updateCurrentNode(node)
    }

    fun backChild() {
        currentNode?.run {
            if (childScreens.size >= 1) {
                val element = childScreens.removeAt(childScreens.size - 1)
                keyManager.remove(element.key)

                updateCurrentNode(this)
                element.onDestroy?.invoke()
            }
        }
    }

    fun backToChild(key: String) {
        currentNode?.run {
            val droppedChildList = dropChildUntil(childScreens, key)

            updateCurrentNode(this)
            droppedChildList.forEach { it.onDestroy?.invoke() }
        }
    }

    fun replaceChild(screen: Screen<*>) {
        currentNode?.run {
            if (childScreens.size >= 1) {
                keyManager.replaceKey(childScreens.last().key, screen.key)
                val oldChildScreen = childScreens[childScreens.size - 1]
                childScreens[childScreens.size - 1] = screen

                childScreens[childScreens.size - 1].run {
                    val dependency = onCreate?.invoke(channel, router)
                    val dependencyProvider = DataProvider(dependency)
                    screen.dependency = dependencyProvider
                }
                updateCurrentNode(this)
                oldChildScreen.onDestroy?.invoke()
            }
        }
    }

    fun addChild(screen: Screen<*>) {
        currentNode?.run {
            keyManager.add(screen.key)
            childScreens.add(screen)

            val dependency = screen.onCreate?.invoke(screen.channel, router)
            val dependencyProvider = DataProvider(dependency)
            screen.dependency = dependencyProvider
            updateCurrentNode(this)
        }
    }

    fun isLastNode(): Boolean = currentNode?.parent == null

    fun closeGraph() {
        currentNode = null
        keyManager.clear()
        updateCurrentNode(currentNode)
        onDestroyNodes(tree)
        tree.clear()
    }

    private fun onDestroyNodes(nodeList: List<Node>) {
        nodeList.forEach { node ->
            node.childScreens.forEach { it.onDestroy?.invoke() }
            node.screen.onDestroy?.invoke()
        }
    }

    private fun dropUntil(node: Node?, screenKey: String) {
        if (node?.screen?.key != screenKey) {
            node?.isActive = false
            dropUntil(node?.parent, screenKey)
        }
    }

    private fun dropChildUntil(
        screens: ArrayList<Screen<*>>,
        screenKey: String
    ): List<Screen<*>> {
        val droppedChildList = ArrayList<Screen<*>>()
        screens.lastOrNull()?.let {
            if (it.key != screenKey) {
                val element = screens.removeAt(screens.size - 1)
                droppedChildList.add(element)
                keyManager.remove(element.key)
                val innerDroppedChildList = dropChildUntil(screens, screenKey)
                droppedChildList.addAll(innerDroppedChildList)
            } else return@let
        }
        return droppedChildList
    }

    private fun updateCurrentNode(node: Node?) {
        currentNode = node
        currentScreenFlow.value = node?.screen
        currentChildFlow.value = node?.childScreens?.toList() ?: emptyList()
    }

    private fun cleanGraph(three: ArrayList<Node>): List<Node> {
        val droppedNodeList = ArrayList<Node>()
        three.removeAll { node ->
            if (!node.isActive) {
                keyManager.remove(node.screen.key)
                node.childScreens.forEach { childScreen -> keyManager.remove(childScreen.key) }
                droppedNodeList.add(node)
            }
            !node.isActive
        }
        three.forEach {
            val innerDroppedNodeList = cleanGraph(it.neighbors)
            droppedNodeList.addAll(innerDroppedNodeList)
        }
        return droppedNodeList
    }
}