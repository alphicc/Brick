package com.navigationtestapp.core

interface TreeRouter : ContainerConnector, ScreenRouter, ChildScreenRouter, ArgumentTranslator {

    val initialScreen: Screen<*>?
    val parentRouter: TreeRouter?

    override fun back()

    fun cleanRouter()

    fun branch(containerScreenKey: String): TreeRouter

    suspend fun <A> passArgument(screenKey: String, argument: A)
}