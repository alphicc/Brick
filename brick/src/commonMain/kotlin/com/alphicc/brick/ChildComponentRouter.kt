package com.alphicc.brick

interface ChildComponentRouter {
    fun lastChildKey(): String?
    fun backChild()
    fun backToChild(key: String)
    fun addChild(component: Component<*>)
    fun <A> addChild(component: Component<*>, argument: A)
    fun replaceChild(component: Component<*>)
    fun <A> replaceChild(component: Component<*>, argument: A)
}