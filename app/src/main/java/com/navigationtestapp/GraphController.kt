package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow

class GraphController {

    val currentNodeFlow: MutableStateFlow<Node?> = MutableStateFlow(null)

    private val keyManager = KeyManager()
    private val tree: ArrayList<Node> = ArrayList()

    private var currentNode: Node? = null

    fun backScreen() {
        currentNode?.isActive = false
        updateCurrentNode(currentNode?.parent)
        cleanGraph(tree)
    }

    fun backToScreen(key: String) {
        dropUntil(currentNode, key)
        updateCurrentNode(currentNode?.parent)
        cleanGraph(tree)
    }

    fun replaceScreen(screen: Screen) {
        currentNode?.let { node -> keyManager.remove(node.screen.key) }
        keyManager.add(screen.key)

        currentNode?.screen = screen
        updateCurrentNode(currentNode)
    }

    fun addScreen(screen: Screen) {
        val weight = (currentNode?.weight ?: 0) + 1
        val node = Node(
            isActive = true,
            screen = screen,
            childScreens = ArrayList(),
            neighbors = ArrayList(),
            weight = weight,
            parent = currentNode
        )
        tree.add(node)
        keyManager.add(screen.key)
        updateCurrentNode(node)
    }

    fun backChild() {
        currentNode?.run {
            if (childScreens.size >= 1) {
                val element = childScreens.removeAt(childScreens.size - 1)
                keyManager.remove(element.key)
            }
            updateCurrentNode(this)
        }
    }

    fun backToChild(key: String) {
        currentNode?.run {
            dropChildUntil(childScreens, key)
            updateCurrentNode(this)
        }
    }

    fun replaceChild(screen: Screen) {
        currentNode?.run {
            if (childScreens.size >= 1) {
                keyManager.replaceKey(childScreens.last().key, screen.key)

                childScreens[childScreens.size - 1] = screen
            }
            updateCurrentNode(this)
        }
    }

    fun addChild(screen: Screen) {
        currentNode?.run {
            childScreens.add(screen)
            keyManager.add(screen.key)
            updateCurrentNode(this)
        }
    }

    fun isLastNode(): Boolean = currentNode?.parent == null

    fun closeGraph() {
        currentNode = null
        tree.clear()
        keyManager.clear()
    }

    private fun dropUntil(node: Node?, screenKey: String) {
        if (node?.screen?.key != screenKey) {
            node?.isActive = false
            dropUntil(node?.parent, screenKey)
        }
    }

    private fun dropChildUntil(screens: ArrayList<Screen>, screenKey: String) {
        screens.lastOrNull()?.let {
            if (it.key != screenKey) {
                val element = screens.removeAt(screens.size - 1)
                keyManager.remove(element.key)
                dropChildUntil(screens, screenKey)
            } else return@let
        }
    }

    private fun updateCurrentNode(node: Node?) {
        currentNode = node
        currentNodeFlow.tryEmit(node)
    }

    private fun cleanGraph(three: ArrayList<Node>) {
        three.removeAll {
            if (!it.isActive) {
                keyManager.remove(it.screen.key)
                it.childScreens.forEach { childScreen -> keyManager.remove(childScreen.key) }
            }
            !it.isActive
        }
        three.forEach { cleanGraph(it.neighbors) }
    }
}