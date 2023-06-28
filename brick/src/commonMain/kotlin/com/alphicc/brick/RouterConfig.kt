package com.alphicc.brick

data class RouterConfig(
    val broadcastFlowReplay: Int,
    val broadcastFlowExtraBufferCapacity: Int
) {
    companion object {
        fun default(): RouterConfig = RouterConfig(broadcastFlowReplay = 1, broadcastFlowExtraBufferCapacity = 10)
    }
}