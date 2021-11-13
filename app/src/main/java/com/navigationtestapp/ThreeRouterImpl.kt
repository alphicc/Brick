package com.navigationtestapp

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

class ThreeRouterImpl : ThreeRouter {

    override val currentNodeFlow: MutableStateFlow<Node?> = MutableStateFlow(null)

    private val three: ArrayList<Node> = ArrayList()
    private val keys: ArrayList<String> = ArrayList()

    private var currentNode: Node? = null

    override fun back(): Boolean {
        currentNode?.run {
            when {
                childScreens.isNotEmpty() -> backChild()
                parent != null -> backScreen()
                else -> {
                    isActive = false
                    currentNode = null
                    cleanGraph(three)
                }
            }
        }
        return currentNode == null
    }

    override fun backScreen() {
        currentNode?.isActive = false
        invalidateCurrentNode(currentNode?.parent)
        cleanGraph(three)
    }

    override fun backToScreen(key: String) {
        dropUntil(currentNode, key)
        invalidateCurrentNode(currentNode?.parent)
        cleanGraph(three)
    }

    override fun replaceScreen(screen: Screen) {
        if (keys.contains(screen.key)) {
            throw IllegalArgumentException("Key ${screen.key} already existed")
        }

        currentNode?.let { node -> keys.remove(node.screen.key) }
        keys.add(screen.key)

        currentNode?.screen = screen
        invalidateCurrentNode(currentNode)

        printKeys()
    }

    override fun branch(screen: Screen) {
        TODO("Not yet implemented")
    }

    override fun addScreen(screen: Screen) {
        if (keys.contains(screen.key)) {
            throw IllegalArgumentException("Key ${screen.key} already existed")
        }

        val weight = (currentNode?.weight ?: 0) + 1
        val node = Node(
            isActive = true,
            screen = screen,
            childScreens = ArrayList(),
            neighbors = ArrayList(),
            weight = weight,
            parent = currentNode
        )
        three.add(node)
        keys.add(screen.key)
        invalidateCurrentNode(node)

        printKeys()
    }

    override fun backChild() {
        currentNode?.run {
            if (childScreens.size >= 1) {
                val element = childScreens.removeAt(childScreens.size - 1)
                keys.remove(element.key)
            }
            invalidateCurrentNode(this)
        }

        printKeys()
    }

    override fun backToChild(key: String) {
        currentNode?.run {
            dropChildUntil(childScreens, key)
            invalidateCurrentNode(this)
        }
    }

    override fun replaceChild(screen: Screen) {
        if (keys.contains(screen.key)) {
            throw IllegalArgumentException("Key ${screen.key} already existed")
        }

        currentNode?.run {
            if (childScreens.size >= 1) {
                keys.remove(childScreens.last().key)
                keys.add(screen.key)

                childScreens[childScreens.size - 1] = screen
            } else addChild(screen)
            invalidateCurrentNode(this)
        }

        printKeys()
    }

    override fun addChild(screen: Screen) {
        if (keys.contains(screen.key)) {
            throw IllegalArgumentException("Key ${screen.key} already existed")
        }

        currentNode?.run {
            childScreens.add(screen)
            keys.add(screen.key)
            invalidateCurrentNode(this)
        }

        printKeys()
    }

    private fun dropChildUntil(screens: ArrayList<Screen>, screenKey: String) {
        screens.lastOrNull()?.let {
            if (it.key != screenKey) {
                val element = screens.removeAt(screens.size - 1)
                keys.remove(element.key)
                dropChildUntil(screens, screenKey)
            } else return@let
        }

        printKeys()
    }

    private fun dropUntil(node: Node?, screenKey: String) {
        if (node?.screen?.key != screenKey) {
            node?.isActive = false
            dropUntil(node?.parent, screenKey)
        }
    }

    private fun cleanGraph(three: ArrayList<Node>) {
        three.removeAll {
            if (!it.isActive) {
                keys.remove(it.screen.key)
                it.childScreens.forEach { childScreen -> keys.remove(childScreen.key) }
            }
            !it.isActive
        }
        three.forEach { cleanGraph(it.neighbors) }

        printKeys()
    }

    private fun invalidateCurrentNode(node: Node?) {
        currentNode = node
        currentNodeFlow.tryEmit(node)
    }

    private fun printKeys() {
        Log.d("Alpha", "==================")
        keys.forEach {
            Log.d("Alpha", "${it}")
        }
        Log.d("Alpha", "==================")
    }

    //4 private fun findActiveNode(nodes: List<Node>): Node {
    //4     nodes.forEach { node ->
    //4         if (node.isActive) return node
    //4         else findActiveNode(node.neighbors)
    //4     }
    //4     throw IllegalStateException("Active Node not found")
    //4 }
//4
    //4 private fun removeActiveNodes(nodes: ArrayList<Node>) {
    //4     nodes.removeAll { it.isActive }
    //4     nodes.forEach { removeActiveNodes(it.neighbors) }
    //4 }

    //  private fun removeActiveNode(nodes: ArrayList<Node>): Boolean = nodes.removeAll { it.isActive }
}