package com.alphicc.brick

import kotlinx.coroutines.CoroutineScope
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
        fun new(coroutineScope: CoroutineScope): TreeRouter =
            TreeRouterImpl(coroutineScope = coroutineScope, config = RouterConfig.default())

        fun new(coroutineScope: CoroutineScope, config: RouterConfig): TreeRouter =
            TreeRouterImpl(coroutineScope = coroutineScope, config = config)
    }
}