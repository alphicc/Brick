package com.alphicc.brick.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.alphicc.brick.AnimDirection
import com.alphicc.brick.AnimState
import com.alphicc.brick.Descriptor
import com.alphicc.brick.Node
import com.alphicc.brick.entities.ComponentUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
@Composable
internal fun RootAnimator(
    nodes: ImmutableList<Node>,
    animState: AnimState,
    backgroundColor: Color,
    frameRate: Int,
    componentsUiState: ImmutableMap<String, ComponentUiState>,
    navigateBack: (immediately: Boolean) -> Unit
) {

    val bus = remember { MutableSharedFlow<UpdateActions>(extraBufferCapacity = Int.MAX_VALUE) }
    LaunchedEffect(animState) {
        val action = when (animState.direction) {
            AnimDirection.FORWARD -> UpdateActions.Triggers.ForwardAction(
                animState.newKey,
                animState.oldKeys
            )

            AnimDirection.BACK -> UpdateActions.Triggers.BackAction(
                animState.newKey,
                animState.oldKeys
            )

            AnimDirection.ON_PLACE -> UpdateActions.None
        }
        bus.emit(action)
    }

    nodes.forEachIndexed { index, keepAliveNode ->
        key(keepAliveNode.mainComponent.key) {

            if (keepAliveNode.mainComponent.descriptor is Descriptor.Screen) {
                val communicator = remember(keepAliveNode.mainComponent.key) {
                    val filteredBus = bus.filter { updateAction ->
                        if (updateAction is UpdateActions.Triggers.BackAction) {
                            updateAction.oldKey.firstOrNull() == keepAliveNode.mainComponent.key
                        } else updateAction.keys.contains(keepAliveNode.mainComponent.key)
                    }
                    GestureCommunicator(
                        bus = filteredBus,
                        tryPublish = { updateAction -> bus.tryEmit(updateAction) },
                        publish = { updateAction -> bus.emit(updateAction) },
                        publishAll = { updateActions ->
                            updateActions.forEach { updateAction ->
//                                println("publish updateAction $updateAction")
                                bus.emit(updateAction)
                            }
                        }
                    )
                }

                FrameAnimator(
                    modifier = Modifier.fillMaxSize().background(backgroundColor),
                    componentUiState = remember(keepAliveNode.mainComponent.key) { componentsUiState[keepAliveNode.mainComponent.key] },
                    key = keepAliveNode.mainComponent.key,
                    index = index,
                    descriptor = keepAliveNode.mainComponent.descriptor,
                    frameRate = frameRate,
                    gestureCommunicator = communicator,
                    navigateBack = navigateBack,
                    requestKey = { index -> nodes.getOrNull(index)?.mainComponent?.key },
                    content = {
                        keepAliveNode.mainComponent.run {
                            showContent(
                                dataContainer = dependency
                                    ?: throw IllegalArgumentException("Dependency can not be null"),
                                compositions = keepAliveNode.compositions
                            )
                        }
                        keepAliveNode.childComponentsList.forEach { childScreen ->
                            childScreen.showContent(
                                dataContainer = childScreen.dependency
                                    ?: throw IllegalArgumentException("Dependency can not be null"),
                                compositions = keepAliveNode.compositions
                            )
                        }
                    }
                )
            }
        }
    }
}