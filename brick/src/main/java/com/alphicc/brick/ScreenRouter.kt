package com.alphicc.brick

interface ScreenRouter {
    fun backScreen()
    fun backToScreen(key: String)
    fun replaceScreen(screen: Screen<*>)
    fun addScreen(screen: Screen<*>)
    fun <A> replaceScreen(screen: Screen<*>, argument: A)
    fun <A> addScreen(screen: Screen<*>, argument: A)
}