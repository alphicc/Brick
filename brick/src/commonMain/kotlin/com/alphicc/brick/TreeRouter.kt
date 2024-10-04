package com.alphicc.brick

import com.alphicc.brick.entities.TransactionScope
import com.alphicc.brick.ui.ContainerConnector
import kotlinx.coroutines.flow.SharedFlow

interface TreeRouter : ContainerConnector, ComponentRouter, ChildComponentRouter,
    CompositeComponentRouter, ArgumentTranslator {

    val parentRouter: TreeRouter?
    val broadcastArgumentsFlow: SharedFlow<Any>

    fun getRootRouter(): TreeRouter

    fun startTransaction(
        transactionSettings: TransactionSettings = TransactionSettings.default(),
        action: TransactionScope.() -> Unit
    )

    fun proceedAction(
        transactionSettings: TransactionSettings = TransactionSettings.immediately(),
        action: () -> Unit
    )

    fun TransactionScope.cleanRouter()

    fun TransactionScope.setOverlay(component: Component<*>)

    fun <A> TransactionScope.setOverlay(component: Component<*>, argument: A)

    fun TransactionScope.removeOverlay()

    suspend fun branch(containerComponentKey: String): TreeRouter

    suspend fun branch(containerComponentKey: String, config: RouterConfig): TreeRouter

    suspend fun <A> passArgument(componentKey: String, argument: A)

    suspend fun <A> passBroadcastArgument(argument: A)

    companion object {
        fun new(): TreeRouter = TreeRouterImpl(config = RouterConfig.default())
        fun new(config: RouterConfig): TreeRouter = TreeRouterImpl(config = config)
    }
}