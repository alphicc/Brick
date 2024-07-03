package com.alphicc.brick.ui

import androidx.compose.runtime.Immutable
import com.alphicc.brick.AnimState
import com.alphicc.brick.Component
import com.alphicc.brick.InternalApi
import com.alphicc.brick.KeepAliveNode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.StateFlow

@Immutable
interface ContainerConnector {
    val overlay: StateFlow<Component<*>?>
    val mainComponent: StateFlow<Component<*>?>
    val childComponents: StateFlow<ImmutableList<Component<*>>>
    val compositions: StateFlow<ImmutableMap<String, Component<*>>>
    val keepAliveNodes: StateFlow<ImmutableList<KeepAliveNode>>
    val isRouterEmpty: StateFlow<Boolean>

    @InternalApi
    val topComponentKey: StateFlow<String?>

    @InternalApi
    val animState: StateFlow<AnimState>

    fun onBackClicked()
}