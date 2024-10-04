package com.alphicc.brick

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

data class RouterConfig(
    val scope: CoroutineScope,
    val broadcastFlowReplay: Int,
    val broadcastFlowExtraBufferCapacity: Int,
) {
    companion object {
        fun default(): RouterConfig = RouterConfig(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            broadcastFlowReplay = 1,
            broadcastFlowExtraBufferCapacity = 10,
        )
    }
}