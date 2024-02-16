package com.alphicc.brick.ui.gesture

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun rememberSwipeGestureDescription(): SwipeGestureDescription {
    return remember { SwipeGestureDescription() }
}

class SwipeGestureDescription : GestureDescription() {

    private var screenWidth: Float? = null
    private val initialTopOffsetX = .0f
    private val topOffsetX = MutableStateFlow(initialTopOffsetX)

    override val bottomGesture: Modifier
        @Composable
        get() {
            return Modifier
        }

    override val topGesture: Modifier
        @Composable
        get() {
            val offsetX by topOffsetX.collectAsState(0.0f)

            return Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    if (screenWidth == null) {
                        val rect = layoutCoordinates.boundsInRoot()
                        screenWidth = rect.topRight.x
                    }
                }
                .offset(x = with(LocalDensity.current) { offsetX.toInt().toDp() })
                .draggable(
                    enabled = true,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        println("onDragStopped=$it")
                    },
                    state = rememberDraggableState {
                        println("state=$it / offsetX=$offsetX")
                        val offset = topOffsetX.value + it
                        if (offset > 0) topOffsetX.value = offset
                    }
                )
        }
}