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
import androidx.compose.ui.layout.onGloballyPositioned
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
    index: Int,
    descriptor: Descriptor.Main,
    gestureCommunicator: GestureCommunicator,
    navigateBack: (immediately: Boolean) -> Unit,
    requestKey: (index: Int) -> String?,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {

    val frameState = rememberSaveable { mutableStateOf(0) }
    val gestureEvent by gestureCommunicator.bus.collectAsState(UpdateActions.None)

    var screenWidth by mutableStateOf(0)
    var minimumXOffset by mutableStateOf(0f)
    var offsetX by remember { mutableStateOf(0f) }
    val initialAlpha =
        remember(descriptor) { if (descriptor is Descriptor.Main.Default) 1f else 0f }
    var alpha by remember { mutableStateOf(initialAlpha) }

    DisposableEffect(key) {
        println("DisposableEffect $key descriptor=$descriptor")
        onDispose {
            println("onDispose $key")
        }
    }

    LaunchedEffect("AnimationsHandler$key") {
        gestureCommunicator.bus
            .onEach { updateAction ->
                withContext(Dispatchers.Default) {
                    println("RECEIVE $updateAction key=$key")
                    when (updateAction) {
                        is UpdateActions.Animations.Shift.ShiftScene -> {
                            offsetX = screenWidth * updateAction.percent
                            delay(1)
                            if (updateAction.finish) {
                                navigateBack(true)
                            }
                        }

                        is UpdateActions.Animations.Shift.ShiftBackdrop -> {
//                            val percent = 1 - (updateAction.offsetX / screenWidth)
                            offsetX = minimumXOffset * updateAction.percent
                            delay(1)
//                            println("percent=${updateAction.percent}")
                        }

                        is UpdateActions.Animations.SwipeBack -> {
                            println("receive SwipeBack key=$key offsetX=${updateAction.offsetX} minimumXOffset=$minimumXOffset")
                            delay(1)
                            alpha = updateAction.alpha
                            val newOffset = updateAction.offsetX * screenWidth
                            offsetX = if (newOffset < minimumXOffset) {
                                minimumXOffset
                            } else updateAction.offsetX * screenWidth
                        }

                        is UpdateActions.Animations.CrossFade -> {
//                            println("receive crossFade ${updateAction.alpha}")
                            delay(8)
                            alpha = updateAction.alpha
                        }

                        is UpdateActions.Triggers.BackAction -> {
                            val actions = updateAction.getActionsByTrigger(descriptor)
//                            println("receive BackAction start ${actions.size} key=$key")
                            gestureCommunicator.publishAll(actions)
//                            println("receive BackAction end")
                        }

                        is UpdateActions.Triggers.ForwardAction -> {
                            val actions = updateAction.getActionsByTrigger(descriptor)
//                            println("receive ForwardAction start ${actions.size}")
                            gestureCommunicator.publishAll(actions)
//                            println("receive ForwardAction end")
                        }

                        is UpdateActions.None -> Unit
                    }
                }
            }
            .launchIn(this)
    }


    NativeBackHandler(
        enabled = true,
        navigateBack = {
            navigateBack(false)
        }
    )

    println("key=$key offsetX=$offsetX")

    var draggableEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                screenWidth = layoutCoordinates.size.width
                minimumXOffset =
                    layoutCoordinates.size.width * UpdateActions.Animations.SwipeBack.SHIFT_THRESHOLD
            }
            .offset {
                IntOffset.Zero.copy(x = offsetX.roundToInt())
            }
            .draggable(
                enabled = draggableEnabled,
                orientation = Orientation.Horizontal,
                state = rememberDraggableState {
                    if (descriptor is Descriptor.Main.Animated.SwipeBack) {
                        println("key=$key descriptor=$descriptor")
                        val offset = offsetX + it
                        if (offset > 0) offsetX = offset
                        val backdropKey = requestKey(index - 1)
                        backdropKey?.let { key ->
                            val updateAction =
                                UpdateActions.Animations.Shift.ShiftBackdrop.getBackdropAction(
                                    key,
                                    offsetX,
                                    screenWidth.toFloat()
                                )
                            gestureCommunicator.tryPublish(updateAction)
                        }
                    }
                },
                onDragStopped = { velocity: Float ->
                    if (descriptor is Descriptor.Main.Animated.SwipeBack) {
                        val backdropKey = requestKey(index - 1)
                        draggableEnabled = false
                        val speed = when {
                            velocity > 0 || velocity < 0 -> velocity
                            velocity == 0f && offsetX > screenWidth / 2 -> 300f
                            else -> -300f
                        }
                        val actions =
                            UpdateActions.Animations.Shift.ShiftScene.getActionsByVelocity(
                                key,
                                backdropKey.orEmpty(),
                                offsetX,
                                speed,
                                screenWidth.toFloat()
                            )
                        gestureCommunicator.publishAll(actions)
                        draggableEnabled = true
                    }

                    println("velocity = $velocity offsetX=$offsetX screenWidth=$screenWidth")
                }
            )
            .alpha(alpha)
    ) {
        content.invoke()
    }
}