package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface TreeRouter : ScreenRouter, ChildScreenRouter {

    val currentNode: StateFlow<Node?>
    val parentRouter: TreeRouter?

    fun back(): Boolean
}