package com.alphicc.brick

data class KeepAliveNode(
    val mainComponent: Component<*>?,
    val childComponentsList: List<Component<*>>,
    val compositions: Map<String, Component<*>>
)