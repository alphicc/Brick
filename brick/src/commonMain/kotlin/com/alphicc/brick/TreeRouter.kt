package com.alphicc.brick

interface TreeRouter : ContainerConnector, CompositeScreenRouter, ScreenRouter, ChildScreenRouter, ArgumentTranslator {

    val initialScreen: Screen<*>?
    val parentRouter: TreeRouter?

    fun getRootRouter(): TreeRouter

    fun cleanRouter()

    fun branch(containerScreenKey: String): TreeRouter

    fun setOverlay(screen: Screen<*>)

    fun <A> setOverlay(screen: Screen<*>, argument: A)

    fun removeOverlay()

    suspend fun <A> passArgument(screenKey: String, argument: A)

    companion object {
        fun new(): TreeRouter = TreeRouterImpl()
    }
}