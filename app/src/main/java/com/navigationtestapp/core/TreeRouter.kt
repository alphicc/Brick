package com.navigationtestapp.core

interface TreeRouter : ContainerConnector, ScreenRouter, ChildScreenRouter {

    val initialScreen: Screen<*>?
    val parentRouter: TreeRouter?

    //  val childRouter: StateFlow<TreeRouter?>

    override fun back()

    fun branch(key: String): TreeRouter
}