package com.alphicc.brick

import kotlinx.collections.immutable.ImmutableMap

data class KeepAliveNode(
    val mainComponent: Component<*>?,
    val childComponentsList: List<Component<*>>,
    val compositions: ImmutableMap<String, Component<*>>
)