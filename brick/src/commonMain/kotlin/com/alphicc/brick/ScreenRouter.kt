package com.alphicc.brick

interface ScreenRouter {
    fun currentScreenKey(): String?
    fun backScreen()
    fun backToScreen(key: String)
    fun replaceScreen(screen: Screen<*>)
    fun <A> replaceScreen(screen: Screen<*>, argument: A)
    fun addScreen(screen: Screen<*>)
    fun <A> addScreen(screen: Screen<*>, argument: A)
    fun newRootScreen(screen: Screen<*>)
    fun <A> newRootScreen(screen: Screen<*>, argument: A)
}