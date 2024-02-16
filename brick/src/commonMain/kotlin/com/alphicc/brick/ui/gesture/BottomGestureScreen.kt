package com.alphicc.brick.ui.gesture

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable

@Composable
fun BottomGestureScreen(gestureDescription: GestureDescription, content: @Composable () -> Unit) {
    Box(modifier = gestureDescription.bottomGesture) {
        content.invoke()
    }
}