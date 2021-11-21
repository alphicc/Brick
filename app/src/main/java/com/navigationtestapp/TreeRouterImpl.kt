package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TreeRouterImpl(
    override val initialScreen: Screen<*>? = null,
    override val parentRouter: TreeRouter? = null
) : TreeRouter {

    private val graphController: GraphController = GraphController()

    override val currentChildRouter: MutableStateFlow<TreeRouter?> = MutableStateFlow(null)
    override val currentNode: MutableStateFlow<Node?> = graphController.currentNodeFlow
    override val innerNodeConnector: StateFlow<ContainerConnector?> = currentChildRouter

    private val childRouters = HashMap<String, TreeRouter>()

    init {
        initialScreen?.let {
            graphController.addScreen(it)
        }
    }

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

    override fun branch(key: String, initialScreen: Screen<*>?): TreeRouter {
        val childRouter = childRouters.getOrPut(key) {
            TreeRouterImpl(initialScreen, this)
        }
        currentChildRouter.value = childRouter
        return childRouter
    }

    override fun backScreen() {
        //if (currentNode.value?.router != null) {
//
        //}
        //graphController.backScreen()
    }

    override fun backToScreen(key: String) = graphController.backToScreen(key)

    override fun replaceScreen(screen: Screen<*>) {
        graphController.replaceScreen(screen)
    }

   // override fun <A> replaceScreen(screen: Screen<*>, argument: A) {
   //     graphController.replaceScreen(screen, argument)
   // }

    override fun addScreen(screen: Screen<*>) = graphController.addScreen(screen)

   //override fun <A> addScreen(screen: Screen<A, *>, argument: A) {
   //}

    // override fun addContainerScreen(containerScreen: ContainerScreen) {
    //     val screen = Screen(containerScreen.key, containerScreen.content)
    //     graphController.addScreen(screen, TreeRouterImpl())
    // }

    override fun backChild() = graphController.backChild()

    override fun backToChild(key: String) = graphController.backToChild(key)

    override fun replaceChild(screen: Screen<*>) = graphController.replaceChild(screen)

    override fun addChild(screen: Screen<*>) = graphController.addChild(screen)
}