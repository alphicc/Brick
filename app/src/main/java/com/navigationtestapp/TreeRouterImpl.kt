package com.navigationtestapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TreeRouterImpl(
    override val initialScreen: Screen<*>? = null,
    override val parentRouter: TreeRouter? = null
) : TreeRouter {

    private val graphControllerImpl: GraphControllerImpl = GraphControllerImpl()

    override val childRouter: MutableStateFlow<TreeRouter?> = MutableStateFlow(null)

    override val screen: MutableStateFlow<Screen<*>?> = graphControllerImpl.currentScreenFlow
    override val childList: StateFlow<List<Screen<*>>> = graphControllerImpl.currentChildFlow

    private val childRouters = HashMap<String, TreeRouter>()

    init {
        initialScreen?.let {
            graphControllerImpl.addScreen(it, this)
        }
    }

    override fun back(): Boolean {
        //when {
        //    node.value?.childScreens?.isNotEmpty() == true -> {
        //        backChild()
        //    }
        //    graphController.isLastNode() -> {
        //        graphController.closeGraph()
        //        return true
        //    }
        //    else -> backScreen()
        //}

        return false
    }

    override fun branch(key: String, initialScreen: Screen<*>?): TreeRouter {
        val childRouter = childRouters.getOrPut(key) {
            TreeRouterImpl(initialScreen, this)
        }
       // this.childRouter.value = childRouter
        return childRouter
    }

    override fun backScreen() {
        //if (currentNode.value?.router != null) {
//
        //}
        graphControllerImpl.backScreen()
    }

    override fun backToScreen(key: String) = graphControllerImpl.backToScreen(key)

    override fun replaceScreen(screen: Screen<*>) {
        graphControllerImpl.replaceScreen(screen)
    }

    override fun addScreen(screen: Screen<*>) = graphControllerImpl.addScreen(screen, this)

    override fun backChild() = graphControllerImpl.backChild()

    override fun backToChild(key: String) = graphControllerImpl.backToChild(key)

    override fun replaceChild(screen: Screen<*>) = graphControllerImpl.replaceChild(screen)

    override fun addChild(screen: Screen<*>) = graphControllerImpl.addChild(screen)
}