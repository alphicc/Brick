package com.alphicc.brick.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import com.alphicc.brick.Descriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.math.roundToInt
import kotlin.native.HiddenFromObjC

//заход в экран контроллится rememberSaveable
//заход в экран и выход сопровождается публикацией иветов в шину, подписчики по индексу получают необходимые данные
@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
@Composable
internal fun FrameAnimator(
    key: String,
    descriptor: Descriptor.Main.RetainUi,
    gestureCommunicator: GestureCommunicator,
    content: @Composable () -> Unit
) {

    val frameState = rememberSaveable { mutableStateOf(0) }
    val gestureEvent by gestureCommunicator.bus.collectAsState(UpdateActions.None)

    val offsetX = rememberSaveable { mutableStateOf(0f) }
    var alpha by remember { mutableStateOf(0f) }

    DisposableEffect(key) {
        println("DisposableEffect $key")
        onDispose {
            println("onDispose $key")
        }
    }

    LaunchedEffect("AnimationsHandler$key") {
        gestureCommunicator.bus
            .onEach { updateAction ->
                withContext(Dispatchers.Default) {
                    println("RECEIVE $updateAction")
                    when (updateAction) {
                        is UpdateActions.Animations.CrossFade -> {
                            println("receive crossFade ${updateAction.alpha}")
                            delay(8)
                            alpha = updateAction.alpha
                        }

                        is UpdateActions.Triggers.BackAction -> {
                            val actions = updateAction.getActionsByTrigger(descriptor)
                            println("receive BackAction start ${actions.size}")
                            gestureCommunicator.publishAll(actions)
                            println("receive BackAction end")
                        }
                        is UpdateActions.Triggers.ForwardAction -> {
                            val actions = updateAction.getActionsByTrigger(descriptor)
                            println("receive ForwardAction start ${actions.size}")
                            gestureCommunicator.publishAll(actions)
                            println("receive ForwardAction end")
                        }

                        is UpdateActions.None -> Unit
                    }
                }
            }
            .launchIn(this)
//        if (frameState.value == 0) {
//            frameState.value = 1
//            val showAnimations = when (gestureEvent) {
//                is UpdateActions.CrossFade -> (gestureEvent as UpdateActions.CrossFade).getShowActions()
//                else -> emptyList()
//            }
//            showAnimations.forEach { updateAction ->
//                gestureCommunicator.publish(updateAction)
//            }
//        } else if ()
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset.Zero.copy(x = offsetX.value.roundToInt())
            }
            .draggable(
                enabled = true,
                orientation = Orientation.Horizontal,
                state = rememberDraggableState {
                    val offset = offsetX.value + it
                    if (offset > 0) offsetX.value = offset
                }
            )
            .alpha(alpha)
    ) {
        content.invoke()
    }
}