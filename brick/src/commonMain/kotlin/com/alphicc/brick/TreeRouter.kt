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

    fun branch(containerComponentKey: String, config: RouterConfig): TreeRouter

    fun setOverlay(component: Component<*>)

    fun <A> setOverlay(component: Component<*>, argument: A)

    fun removeOverlay()

    suspend fun <A> passArgument(componentKey: String, argument: A)

    suspend fun <A> passBroadcastArgument(argument: A)

    companion object {
        fun new(): TreeRouter = TreeRouterImpl(config = RouterConfig.default())
        fun new(config: RouterConfig): TreeRouter = TreeRouterImpl(config = config)
    }
}