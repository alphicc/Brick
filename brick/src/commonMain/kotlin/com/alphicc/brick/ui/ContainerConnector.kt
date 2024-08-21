package com.alphicc.brick.ui

import androidx.compose.runtime.Stable
import com.alphicc.brick.AnimState
import com.alphicc.brick.Component
import com.alphicc.brick.InternalApi
import com.alphicc.brick.Node
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

@Stable
interface ContainerConnector {
    val overlay: StateFlow<Component<*>?>
    val nodes: StateFlow<ImmutableList<Node>>
    val isRouterEmpty: StateFlow<Boolean>

    @InternalApi
    val topComponentKey: StateFlow<String?>

    @InternalApi
    val animState: StateFlow<AnimState>

    fun onBackClicked(immediately: Boolean = false)
}