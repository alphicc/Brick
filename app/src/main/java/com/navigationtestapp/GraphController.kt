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
        //currentNode?.router?.let {
        //    it.backScreen()
        //    return
        //}

        currentNode?.isActive = false

        updateCurrentNode(currentNode?.parent)
        cleanGraph(tree)
    }

    fun backToScreen(key: String) {
        //currentNode?.router?.let {
        //    it.backToScreen(key)
        //    return
        //}

        dropUntil(currentNode, key)

        updateCurrentNode(currentNode?.parent)
        cleanGraph(tree)
    }

    fun replaceScreen(screen: Screen) {
        //currentNode?.router?.let {
        //    it.replaceScreen(screen)
        //    return
        //}

        currentNode?.let { node -> keyManager.remove(node.screen.key) }
        keyManager.add(screen.key)

        currentNode?.screen = screen

        updateCurrentNode(currentNode)
    }

    fun addScreen(screen: Screen) {
        //currentNode?.router?.let {
        //    it.addScreen(screen)
        //    return
        //}

        keyManager.add(screen.key)

        val node = Node(
            isActive = true,
            screen = screen,
            childScreens = ArrayList(),
            neighbors = ArrayList(),
            parent = currentNode
        )
        tree.add(node)

        updateCurrentNode(node)
    }

    fun backChild() {
        //currentNode?.router?.let {
        //    it.backChild()
        //    return
        //}
//
        currentNode?.run {
            if (childScreens.size >= 1) {
                val element = childScreens.removeAt(childScreens.size - 1)
                keyManager.remove(element.key)
            }
            updateCurrentNode(this)
        }
    }

    fun backToChild(key: String) {
        //currentNode?.router?.let {
        //    it.backToChild(key)
        //    return
        //}

        currentNode?.run {
            dropChildUntil(childScreens, key)
            updateCurrentNode(this)
        }
    }

    fun replaceChild(screen: Screen) {
        //currentNode?.router?.let {
        //    it.replaceChild(screen)
        //    return
        //}
//
        currentNode?.run {
            if (childScreens.size >= 1) {
                keyManager.replaceKey(childScreens.last().key, screen.key)

                childScreens[childScreens.size - 1] = screen
            }
            updateCurrentNode(this)
        }
    }

    fun addChild(screen: Screen) {
        //currentNode?.router?.let {
        //    it.addChild(screen)
        //    return
        //}

        currentNode?.run {
            keyManager.add(screen.key)

            childScreens.add(screen)

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