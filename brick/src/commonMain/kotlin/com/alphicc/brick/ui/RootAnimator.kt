package com.alphicc.brick.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import com.alphicc.brick.AnimDirection
import com.alphicc.brick.AnimState
import com.alphicc.brick.Descriptor
import com.alphicc.brick.KeepAliveNode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

private const val HOLD_UI_SCREENS_COUNT = 3

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
@Composable
internal fun RootAnimator(
    contentHidden: Boolean,
    keepAliveNodes: ImmutableList<KeepAliveNode>,
    animState: AnimState
) {
    DisposableEffect("key") {
        println("DisposableEffect RootAnimator")
        onDispose {
            println("onDispose RootAnimator")
        }
    }

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
        println("emit success")
    }

//    println("keepAliveNodes size=${keepAliveNodes.size}")
//    keepAliveNodes.forEach {
//        println("printNode ${it.mainComponent.key}")
//    }


    keepAliveNodes.forEachIndexed { index, keepAliveNode ->
        DisposableEffect("31") {
            println("DisposableEffect == ${keepAliveNode.mainComponent.key} index=$index size=${keepAliveNodes.size}")
            keepAliveNodes.forEach {
                println("keepAliveNode ==${it.mainComponent.key}")
            }
            onDispose {
                println("onDispose == ${keepAliveNode.mainComponent.key} index=$index size=${keepAliveNodes.size}")
            }
        }
        key(keepAliveNode.mainComponent.key) {
//        LaunchedEffect("ud") {
//            println("Attach ${keepAliveNode.mainComponent.key}")
//        }
//        val (isVisible, isLastElement) = remember(key1 = keepAliveNodes.size) {
//            val lastElementIndex = keepAliveNodes.size - 1
//            (/*index >= lastElementIndex - HOLD_UI_SCREENS_COUNT*/ true) to (index == lastElementIndex)
//        }

            if (keepAliveNode.mainComponent.descriptor is Descriptor.Main.RetainUi) {
                val communicator = remember(keepAliveNode.mainComponent.key) {
                    println("remember call=${keepAliveNode.mainComponent.key}")
                    val filteredBus = bus.filter { updateAction ->
//                        println("bus filter $updateAction ${updateAction.key} ${keepAliveNode.mainComponent.key}")
//                    println("filter 1=${updateAction.key} 2=${keepAliveNode.mainComponent.key}")
                        updateAction.key == keepAliveNode.mainComponent.key
                    }
                    GestureCommunicator(
                        bus = filteredBus,
                        publish = { updateAction ->
                            bus.emit(updateAction)
                        },
                        publishAll = { updateActions ->
                            updateActions.forEach { updateAction ->
                                bus.emit(updateAction)
                            }
                        }
                    )
                }
                //val gestureEvent by communicator.bus.collectAsState(UpdateActions.None)

//            LaunchedEffect("TriggersHandler") {
//                communicator.bus
//                    .filterIsInstance<UpdateActions.Triggers>()
//                    .onEach { triggers: UpdateActions.Triggers ->
//                        withContext(Dispatchers.Default) {
//                            when(keepAliveNode.mainComponent.descriptor) {
//                                is Descriptor.Main.RetainUi.CrossFade -> {
//                                    UpdateActions.Animations.CrossFade
//                                }
//
//                                is Descriptor.Main.RetainUi.SwipeBack -> {
//
//                                }
//                                is Descriptor.Main.RetainUi.SwipeDown -> {
//
//                                }
//                            }
//                            when (triggers) {
//                                is UpdateActions.Triggers.BackAction -> {
//
//                                }
//                                is UpdateActions.Triggers.ForwardAction -> TODO()
//                            }
//                        }
//                    }.launchIn(this)
//            }

//            val navigateBackLaunched by delayedBackNavigation.collectAsState()
//            val navigateBack = remember(
//                key1 = isLastElement,
//                key2 = navigateBackLaunched
//            ) { isLastElement && navigateBackLaunched }

                println("FrameAnimator call=${keepAliveNode.mainComponent.key} ?=${keepAliveNode.hashCode()} / ${communicator}")
                FrameAnimator(
                    keepAliveNode.mainComponent.key,
                    keepAliveNode.mainComponent.descriptor,
                    communicator
                ) {
                    if (contentHidden) {
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
                }
            }
        }
    }
}