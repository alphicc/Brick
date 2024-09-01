package com.alphicc.brick.ui

import androidx.compose.foundation.background
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
import kotlinx.collections.immutable.ImmutableList
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
    navigateBack: (immediately: Boolean) -> Unit
) {

    val bus = remember { MutableSharedFlow<UpdateActions>(extraBufferCapacity = Int.MAX_VALUE) }
    LaunchedEffect(animState) {
        println("new animState=${animState.direction} ${animState}")
        val action = when (animState.direction) {
            AnimDirection.FORWARD -> UpdateActions.Triggers.ForwardAction(
                animState.newKey,
                animState.oldKeys
            )

            AnimDirection.BACK -> UpdateActions.Triggers.BackAction(
                animState.newKey,
                animState.oldKeys
            )

            AnimDirection.NONE -> UpdateActions.None
        }
        println("new Action=$action ${bus.subscriptionCount.value}")
        bus.emit(action)
    }

    nodes.forEachIndexed { index, keepAliveNode ->
//        DisposableEffect("31") {
//            println("DisposableEffect == ${keepAliveNode.mainComponent.key} index=$index size=${nodes.size}")
//            nodes.forEach {
//                println("keepAliveNode ==${it.mainComponent.key}")
//            }
//            onDispose {
//                println("onDispose == ${keepAliveNode.mainComponent.key} index=$index size=${nodes.size}")
//            }
//        }
        key(keepAliveNode.mainComponent.key) {

            if (keepAliveNode.mainComponent.descriptor is Descriptor.Main) {
                val communicator = remember(keepAliveNode.mainComponent.key) {
//                    println("remember call=${keepAliveNode.mainComponent.key}")
                    val filteredBus = bus.filter { updateAction ->
//                        println("bus filter $updateAction ${updateAction.keys} ${keepAliveNode.mainComponent.key}")
//                        println("filter 1=${updateAction.keys} 2=${keepAliveNode.mainComponent.key}")
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

//                println("FrameAnimator call=${keepAliveNode.mainComponent.key} ${keepAliveNode.mainComponent.descriptor}")
                FrameAnimator(
                    modifier = Modifier.background(backgroundColor),
                    key = keepAliveNode.mainComponent.key,
                    index = index,
                    descriptor = keepAliveNode.mainComponent.descriptor,
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