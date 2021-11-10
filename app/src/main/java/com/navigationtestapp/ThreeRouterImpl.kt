package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow

class ThreeRouterImpl : ThreeRouter {

    override val currentNodeFlow: MutableStateFlow<Node?> = MutableStateFlow(null)

    private val three: ArrayList<Node> = ArrayList()
    private val keys: ArrayList<String> = ArrayList()

    private var currentNode: Node? = null

    override fun back() {
        currentNode?.isActive = false
        setCurrentNode(currentNode?.parent)
        cleanGraph(three)
    }

    override fun backTo(key: String) {
        dropUntil(currentNode, key)
        cleanGraph(three)
    }

    override fun replace(screen: Screen) {
        currentNode?.let { node -> keys.remove(node.screen.key) }
        keys.add(screen.key)

        currentNode?.screen = screen
        setCurrentNode(currentNode)
    }

    override fun branch(screen: Screen) {
        TODO("Not yet implemented")
    }

    override fun new(screen: Screen) {
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
        setCurrentNode(node)
    }

    private fun dropUntil(node: Node?, screenKey: String) {
        if (node?.screen?.key == screenKey) {
            setCurrentNode(node)
        } else {
            node?.isActive = false
            dropUntil(node?.parent, screenKey)
        }
    }

    private fun cleanGraph(three: ArrayList<Node>) {
        three.removeAll {
            if (!it.isActive) keys.remove(it.screen.key)
            !it.isActive
        }
        three.forEach { cleanGraph(it.neighbors) }
    }

    private fun setCurrentNode(node: Node?) {
        currentNode = node
        currentNodeFlow.tryEmit(node)
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