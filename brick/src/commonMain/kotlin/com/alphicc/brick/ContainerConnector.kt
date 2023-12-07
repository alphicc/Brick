package com.alphicc.brick

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.StateFlow

interface ContainerConnector {
    val overlay: StateFlow<Component<*>?>
    val mainComponent: StateFlow<Component<*>?>
    val childComponentsList: StateFlow<ImmutableList<Component<*>>>
    val compositions: StateFlow<ImmutableMap<String, Component<*>>>
    val keepAliveNodes: StateFlow<ImmutableList<KeepAliveNode>>
    val isRouterEmpty: StateFlow<Boolean>

    fun onBackClicked()
}