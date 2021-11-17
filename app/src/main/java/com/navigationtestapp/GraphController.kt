package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow

class GraphController {

    val currentNodeFlow: MutableStateFlow<Node?> = MutableStateFlow(null)

    private val keyManager = KeyManager()
    private val tree: ArrayList<Node> = ArrayList()

    private var currentNode: Node? = null

    fun branch(screen: Screen) {
        //currentNode?.neighbors?.forEach {
        //    if (it.screen.key == screen.key) {
//
        //    }
        //}
        //currentNode?.neighbors?.add()
    }

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

    fun replaceScreen(screen: Screen) {
        currentNode?.let { node -> keyManager.remove(node.screen.key) }
        keyManager.add(screen.key)

        val oldScreen = currentNode?.screen
        currentNode?.screen = screen

        currentNode?.screen?.onCreate?.invoke()
        updateCurrentNode(currentNode)
        oldScreen?.onDestroy?.invoke()
    }

    fun addScreen(screen: Screen) {
        keyManager.add(screen.key)

        val node = Node(
            isActive = true,
            screen = screen,
            childScreens = ArrayList(),
            neighbors = ArrayList(),
            parent = currentNode
        )
        tree.add(node)

        node.screen.onCreate.invoke()
        updateCurrentNode(node)
    }

    fun backChild() {
        currentNode?.run {
            if (childScreens.size >= 1) {
                val element = childScreens.removeAt(childScreens.size - 1)
                keyManager.remove(element.key)

                updateCurrentNode(this)
                element.onDestroy.invoke()
            }
        }
    }

    fun backToChild(key: String) {
        currentNode?.run {
            val droppedChildList = dropChildUntil(childScreens, key)

            updateCurrentNode(this)
            droppedChildList.forEach { it.onDestroy.invoke() }
        }
    }

    fun replaceChild(screen: Screen) {
        currentNode?.run {
            if (childScreens.size >= 1) {
                keyManager.replaceKey(childScreens.last().key, screen.key)
                val oldChildScreen = childScreens[childScreens.size - 1]
                childScreens[childScreens.size - 1] = screen

                childScreens[childScreens.size - 1].onCreate.invoke()
                updateCurrentNode(this)
                oldChildScreen.onDestroy.invoke()
            }
        }
    }

    fun addChild(screen: Screen) {
        currentNode?.run {
            keyManager.add(screen.key)
            childScreens.add(screen)

            screen.onCreate.invoke()
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
            node.childScreens.forEach { it.onDestroy.invoke() }
            node.screen.onDestroy.invoke()
        }
    }

    private fun dropUntil(node: Node?, screenKey: String) {
        if (node?.screen?.key != screenKey) {
            node?.isActive = false
            dropUntil(node?.parent, screenKey)
        }
    }

    private fun dropChildUntil(screens: ArrayList<Screen>, screenKey: String): List<Screen> {
        val droppedChildList = ArrayList<Screen>()
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
        currentNodeFlow.tryEmit(node)
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