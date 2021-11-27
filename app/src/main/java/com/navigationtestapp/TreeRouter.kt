package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface TreeRouter : ContainerConnector, ScreenRouter, ChildScreenRouter {

    val initialScreen: Screen<*>?
    val parentRouter: TreeRouter?

    val childRouter: StateFlow<TreeRouter?>

    fun back(): Boolean
    fun branch(key: String, initialScreen: Screen<*>? = null): TreeRouter
}