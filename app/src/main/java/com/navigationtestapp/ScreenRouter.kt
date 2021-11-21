package com.navigationtestapp

interface ScreenRouter {
    fun backScreen()
    fun backToScreen(key: String)
    fun replaceScreen(screen: Screen<*>)
    fun addScreen(screen: Screen<*>)
}