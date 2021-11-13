package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface TreeRouter : ScreenRouter, ChildScreenRouter {

    val currentNode: StateFlow<Node?>

    fun back(): Boolean
}