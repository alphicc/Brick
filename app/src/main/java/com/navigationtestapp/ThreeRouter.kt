package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface ThreeRouter {

    val currentNodeFlow: StateFlow<Node?>

    //common
    fun back(): Boolean

    //container navigation
    fun backScreen()
    fun backToScreen(key: String)
    fun replaceScreen(screen: Screen)
    fun branch(screen: Screen)
    fun addScreen(screen: Screen)

    //child navigation
    fun backChild()
    fun backToChild(key: String)
    fun replaceChild(screen: Screen)
    fun addChild(screen: Screen)
}