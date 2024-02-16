package com.alphicc.brick.ui.gesture

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable

@Composable
fun TopGestureScreen(gestureDescription: GestureDescription, content: @Composable () -> Unit) {
    Box(modifier = gestureDescription.topGesture) {
        content.invoke()
    }
}