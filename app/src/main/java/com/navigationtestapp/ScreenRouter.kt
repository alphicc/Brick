package com.navigationtestapp

interface ScreenRouter {
    fun branch(screen: Screen)
    fun backScreen()
    fun backToScreen(key: String)
    fun replaceScreen(screen: Screen)
    fun addScreen(screen: Screen)
}