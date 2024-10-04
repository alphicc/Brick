package com.alphicc.brick

import com.alphicc.brick.entities.TransactionScope

interface ChildComponentRouter {
    fun lastChildKey(): String?
    fun TransactionScope.backChild()
    fun TransactionScope.backToChild(key: String)
    fun TransactionScope.addChild(component: Component<*>)
    fun <A> TransactionScope.addChild(component: Component<*>, argument: A)
    fun TransactionScope.replaceChild(component: Component<*>)
    fun <A> TransactionScope.replaceChild(component: Component<*>, argument: A)
}