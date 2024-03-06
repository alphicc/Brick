package com.alphicc.brick

interface ComponentRouter {
    fun lastComponentKey(): String?
    fun backComponent()
    fun backToComponent(key: String)
    fun replaceComponent(component: Component<*>)
    fun <A> replaceComponent(component: Component<*>, argument: A)
    fun addComponent(component: Component<*>)
    fun <A> addComponent(component: Component<*>, argument: A)
    fun newRootComponent(component: Component<*>)
    fun <A> newRootComponent(component: Component<*>, argument: A)
}