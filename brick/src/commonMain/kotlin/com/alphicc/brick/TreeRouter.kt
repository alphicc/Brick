package com.alphicc.brick

import com.alphicc.brick.ui.ContainerConnector
import kotlinx.coroutines.flow.SharedFlow

interface TreeRouter : ContainerConnector, ComponentRouter, ChildComponentRouter,
    CompositeComponentRouter,
    ArgumentTranslator {

    val parentRouter: TreeRouter?
    val broadcastArgumentsFlow: SharedFlow<Any>

    fun getRootRouter(): TreeRouter

    fun cleanRouter()

    fun setOverlay(component: Component<*>)

    fun <A> setOverlay(component: Component<*>, argument: A)

    fun removeOverlay()

    suspend fun branch(containerComponentKey: String): TreeRouter

    suspend fun branch(containerComponentKey: String, config: RouterConfig): TreeRouter

    suspend fun <A> passArgument(componentKey: String, argument: A)

    suspend fun <A> passBroadcastArgument(argument: A)

    companion object {
        fun new(): TreeRouter = TreeRouterImpl(config = RouterConfig.default())
        fun new(config: RouterConfig): TreeRouter = TreeRouterImpl(config = config)
    }
}