package com.alphicc.brick

import com.alphicc.brick.entities.TransactionScope

interface ComponentRouter {
    fun lastComponentKey(): String?
    fun TransactionScope.backComponent()
    fun TransactionScope.backToComponent(key: String)
    fun TransactionScope.replaceComponent(component: Component<*>)
    fun <A> TransactionScope.replaceComponent(component: Component<*>, argument: A)
    fun TransactionScope.addComponent(component: Component<*>)
    fun <A> TransactionScope.addComponent(component: Component<*>, argument: A)
    fun TransactionScope.newRootComponent(component: Component<*>)
    fun <A> TransactionScope.newRootComponent(component: Component<*>, argument: A)
}