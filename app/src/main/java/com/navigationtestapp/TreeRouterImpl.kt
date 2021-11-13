package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow

class TreeRouterImpl : TreeRouter {

    private val graphController: GraphController = GraphController()

    override val currentNode: MutableStateFlow<Node?> = graphController.currentNodeFlow

    override fun back(): Boolean {
        when {
            currentNode.value?.childScreens?.isNotEmpty() == true -> {
                backChild()
            }
            graphController.isLastNode() -> {
                graphController.closeGraph()
                return true
            }
            else -> backScreen()
        }

        return false
    }

    override fun branch(screen: Screen) {
        TODO("Not yet implemented")
    }

    override fun backScreen() = graphController.backScreen()

    override fun backToScreen(key: String) = graphController.backToScreen(key)

    override fun replaceScreen(screen: Screen) = graphController.replaceScreen(screen)

    override fun addScreen(screen: Screen) = graphController.addScreen(screen)

    override fun backChild() = graphController.backChild()

    override fun backToChild(key: String) = graphController.backToChild(key)

    override fun replaceChild(screen: Screen) = graphController.replaceChild(screen)

    override fun addChild(screen: Screen) = graphController.addChild(screen)
}