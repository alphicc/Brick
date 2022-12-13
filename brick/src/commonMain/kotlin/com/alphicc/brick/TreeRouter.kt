package com.alphicc.brick

import kotlinx.coroutines.flow.SharedFlow

interface TreeRouter : ContainerConnector, CompositeComponentRouter, ComponentRouter, ChildComponentRouter,
    ArgumentTranslator {

    val initialComponent: Component<*>?
    val parentRouter: TreeRouter?
    val broadcastFlow: SharedFlow<Any>

    fun getRootRouter(): TreeRouter

    fun cleanRouter()

    fun branch(containerComponentKey: String): TreeRouter

    fun setOverlay(component: Component<*>)

    fun <A> setOverlay(component: Component<*>, argument: A)

    fun removeOverlay()

    suspend fun <A> passArgument(componentKey: String, argument: A)

    suspend fun <A> passBroadcastArgument(componentKey: String, argument: A)

    companion object {
        fun new(): TreeRouter = TreeRouterImpl()
    }
}