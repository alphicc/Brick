package com.alphicc.brick.ui.gesture

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

abstract class GestureDescription {

    @get:Composable abstract val bottomGesture: Modifier

    @get:Composable abstract val topGesture: Modifier
}