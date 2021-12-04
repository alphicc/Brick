package com.alphicc.brick

interface TreeRouter : ContainerConnector, ScreenRouter, ChildScreenRouter, ArgumentTranslator {

    val initialScreen: Screen<*>?
    val parentRouter: TreeRouter?

    override fun back()

    fun cleanRouter()

    fun branch(containerScreenKey: String): TreeRouter

    suspend fun <A> passArgument(screenKey: String, argument: A)

    companion object {
        fun new(): TreeRouter = TreeRouterImpl()
    }
}