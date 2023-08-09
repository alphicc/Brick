package com.alphicc.brick

import kotlinx.coroutines.flow.StateFlow

interface ContainerConnector {
    val overlay: StateFlow<Component<*>?>
    val mainComponent: StateFlow<Component<*>?>
    val childComponentsList: StateFlow<List<Component<*>>>
    val compositions: StateFlow<Map<String, Component<*>>>
    val keepAliveNodes: StateFlow<List<KeepAliveNode>>
    val isRouterEmpty: StateFlow<Boolean>

    fun onBackClicked()
}