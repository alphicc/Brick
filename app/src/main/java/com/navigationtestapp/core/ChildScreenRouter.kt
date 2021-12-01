package com.navigationtestapp.core

interface ChildScreenRouter {
    fun backChild()
    fun backToChild(key: String)
    fun replaceChild(screen: Screen<*>)
    fun addChild(screen: Screen<*>)
    fun <A> replaceChild(screen: Screen<*>, argument: A)
    fun <A> addChild(screen: Screen<*>, argument: A)
}