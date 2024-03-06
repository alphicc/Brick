package com.alphicc.brick

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

data class KeepAliveNode(
    val mainComponent: Component<*>,
    val childComponentsList: ImmutableList<Component<*>>,
    val compositions: ImmutableMap<String, Component<*>>
)