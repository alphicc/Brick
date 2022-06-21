package com.alphicc.brick

interface CompositeScreenRouter : ScreenRouter {
    fun attachCompositeScreen(screen: Screen<*>)
    fun <A> attachCompositeScreen(screen: Screen<*>, argument: A)
    fun detachCompositeScreen(key: String)
}