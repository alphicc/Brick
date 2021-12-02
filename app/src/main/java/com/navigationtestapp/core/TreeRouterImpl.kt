package com.navigationtestapp.core

import kotlinx.coroutines.flow.StateFlow

class TreeRouterImpl(
    override val initialScreen: Screen<*>? = null,
    override val parentRouter: TreeRouter? = null
) : TreeRouter, GraphEventsInterceptor {

    private val graphController: GraphController = GraphController(this)
    private val childRouters: ArrayList<Pair<String, TreeRouter>> = ArrayList()

    override val screen: StateFlow<Screen<*>?> = graphController.currentScreenFlow

    override val childList: StateFlow<List<Screen<*>>> = graphController.currentChildFlow

    override val hasBackNavigationVariants: StateFlow<Boolean> =
        graphController.hasBackNavigationVariants

    override fun onDestroyScreen(key: String) {
        childRouters.removeAll {
            if (it.first == key) {
                it.second.cleanRouter()
                true
            } else false
        }
    }

    override fun back() = graphController.back()

    override fun cleanRouter() = graphController.cleanGraph()

    override fun branch(containerScreenKey: String): TreeRouter {
        val newRouter = TreeRouterImpl(initialScreen, this)
        childRouters.add(containerScreenKey to newRouter)
        return newRouter
    }

    override fun backScreen() = graphController.backScreen()

    override fun backToScreen(key: String) = graphController.backToScreen(key)

    override fun replaceScreen(screen: Screen<*>) =
        graphController.replaceScreen(screen, null)

    override fun <A> replaceScreen(screen: Screen<*>, argument: A) =
        graphController.replaceScreen(screen, argument)

    override fun addScreen(screen: Screen<*>) =
        graphController.addScreen(screen, null)

    override fun <A> addScreen(screen: Screen<*>, argument: A) =
        graphController.addScreen(screen, argument)

    override fun backChild() = graphController.backChild()

    override fun backToChild(key: String) = graphController.backToChild(key)

    override fun replaceChild(screen: Screen<*>) =
        graphController.replaceChild(screen, null)

    override fun <A> replaceChild(screen: Screen<*>, argument: A) =
        graphController.replaceChild(screen, argument)

    override fun addChild(screen: Screen<*>) =
        graphController.addChild(screen, null)

    override fun <A> addChild(screen: Screen<*>, argument: A) =
        graphController.addChild(screen, argument)
}