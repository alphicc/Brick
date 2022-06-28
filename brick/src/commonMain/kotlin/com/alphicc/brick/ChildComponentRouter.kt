package com.alphicc.brick

interface ChildComponentRouter {
    fun lastChildKey(): String?
    fun backChild()
    fun backToChild(key: String)
    fun replaceChild(component: Component<*>)
    fun addChild(component: Component<*>)
    fun <A> replaceChild(component: Component<*>, argument: A)
    fun <A> addChild(component: Component<*>, argument: A)
}