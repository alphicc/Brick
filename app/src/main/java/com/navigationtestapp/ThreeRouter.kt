package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface ThreeRouter {

    val currentNodeFlow: StateFlow<Node?>

    fun back()
    fun backTo(key: String)
    fun replace(screen: Screen)
    fun branch(screen: Screen)
    fun new(screen: Screen)
}