package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow

class TreeRouterImpl(
    override val initialScreen: Screen<*>? = null,
    override val parentRouter: TreeRouter? = null
) : TreeRouter {

    private val graphController: GraphController = GraphController()

    override val childRouter: MutableStateFlow<TreeRouter?> = MutableStateFlow(null)
    override val node: MutableStateFlow<Node?> = graphController.currentNodeFlow

    private val childRouters = HashMap<String, TreeRouter>()

    init {
        initialScreen?.let {
            graphController.addScreen(it, this)
        }
    }

    override fun back(): Boolean {
        when {
            node.value?.childScreens?.isNotEmpty() == true -> {
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

    override fun branch(key: String, initialScreen: Screen<*>?): TreeRouter {
        val childRouter = childRouters.getOrPut(key) {
            TreeRouterImpl(initialScreen, this)
        }
        this.childRouter.value = childRouter
        return childRouter
    }

    override fun backScreen() {
        //if (currentNode.value?.router != null) {
//
        //}
        graphController.backScreen()
    }

    override fun backToScreen(key: String) = graphController.backToScreen(key)

    override fun replaceScreen(screen: Screen<*>) {
        graphController.replaceScreen(screen)
    }

    override fun addScreen(screen: Screen<*>) = graphController.addScreen(screen, this)

    override fun backChild() = graphController.backChild()

    override fun backToChild(key: String) = graphController.backToChild(key)

    override fun replaceChild(screen: Screen<*>) = graphController.replaceChild(screen)

    override fun addChild(screen: Screen<*>) = graphController.addChild(screen)
}