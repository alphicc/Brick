package com.navigationtestapp.core

interface TreeRouter : ContainerConnector, ScreenRouter, ChildScreenRouter {

    val initialScreen: Screen<*>?
    val parentRouter: TreeRouter?

    override fun back()

    fun cleanRouter()

    fun branch(key: String): TreeRouter
}