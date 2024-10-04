package com.alphicc.brick

import com.alphicc.brick.entities.TransactionScope

interface CompositeComponentRouter : ComponentRouter {
    fun TransactionScope.attachCompositeComponent(component: Component<*>)
    fun <A> TransactionScope.attachCompositeComponent(component: Component<*>, argument: A)
    fun TransactionScope.detachCompositeComponent(key: String)
}