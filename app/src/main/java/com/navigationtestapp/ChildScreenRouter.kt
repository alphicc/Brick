package com.navigationtestapp

interface ChildScreenRouter {
    fun backChild()
    fun backToChild(key: String)
    fun replaceChild(screen: Screen<*>)
    fun addChild(screen: Screen<*>)
}