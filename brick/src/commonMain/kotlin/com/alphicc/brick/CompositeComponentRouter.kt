package com.alphicc.brick

interface CompositeComponentRouter : ComponentRouter {
    fun attachCompositeComponent(component: Component<*>)
    fun <A> attachCompositeComponent(component: Component<*>, argument: A)
    fun detachCompositeComponent(key: String)
}