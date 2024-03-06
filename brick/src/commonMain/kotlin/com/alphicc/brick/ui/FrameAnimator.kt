package com.alphicc.brick.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.alphicc.brick.NavigateAnimation
import kotlin.math.roundToInt

//заход в экран контроллится rememberSaveable
//заход в экран и выход сопровождается публикацией иветов в шину, подписчики по индексу получают необходимые данные
@Composable
fun FrameAnimator(
    mainNavigateAnimation: NavigateAnimation,
    gestureCommunicator: GestureCommunicator,
    content: @Composable () -> Unit
) {

    val offsetX = rememberSaveable { mutableStateOf(0f) }

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
    )
    {
        content.invoke()
    }
}