package com.alphicc.brick

import kotlinx.coroutines.flow.StateFlow

internal class TreeRouterImpl(
    override val initialScreen: Screen<*>? = null,
    override val parentRouter: TreeRouter? = null
) : TreeRouter, GraphEventsInterceptor, ArgumentTranslator {

    private val graphController: GraphController = GraphController(this)
    private val childRouters: ArrayList<Pair<String, TreeRouter>> = ArrayList()

    override val overlay: StateFlow<Screen<*>?> = graphController.currentOverlayFlow

    override val screen: StateFlow<Screen<*>?> = graphController.currentScreenFlow

    override val childList: StateFlow<List<Screen<*>>> = graphController.currentChildFlow

    override val hasBackNavigationVariants: StateFlow<Boolean> =
        graphController.hasBackNavigationVariants

    override fun currentScreenKey(): String? = screen.value?.key

    override fun onDestroyScreen(key: String) {
        childRouters.removeAll {
            if (it.first == key) {
                it.second.cleanRouter()
                true
            } else false
        }
    }

    override fun back() = graphController.back()

    override fun getRootRouter(): TreeRouter = parentRouter?.getRootRouter() ?: this

    override fun cleanRouter() = graphController.cleanGraph()

    override fun branch(containerScreenKey: String): TreeRouter {
        val newRouter = TreeRouterImpl(initialScreen, this)
        childRouters.add(containerScreenKey to newRouter)
        return newRouter
    }

    override fun setOverlay(screen: Screen<*>) {
        val rootRouter = getRootRouter()
        if (rootRouter === this) graphController.setOverlay(screen, null)
    }

    override fun <A> setOverlay(screen: Screen<*>, argument: A) {
        val rootRouter = getRootRouter()
        if (rootRouter === this) graphController.setOverlay(screen, argument)
    }

    override fun removeOverlay() {
        val rootRouter = getRootRouter()
        if (rootRouter === this) graphController.removeOverlay()
    }

    override suspend fun <A> passArgument(screenKey: String, argument: A) {
        redirectArgument(this, screenKey, argument)
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

    override fun lastChildKey(): String? = childList.value.lastOrNull()?.key

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

    override suspend fun <A> redirectArgument(
        from: ArgumentTranslator,
        screenKey: String,
        argument: A
    ) {
        if (parentRouter !== from) {
            parentRouter?.redirectArgument(this, screenKey, argument)
        }
        childRouters.forEach { it.second.redirectArgument(this, screenKey, argument) }
        graphController.passArgument(screenKey, argument)
    }
}