package com.alphicc.brick.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun NativeBackHandler(
    enabled: Boolean,
    navigateBack: () -> Unit
) {
    BackHandler(enabled) {
        navigateBack()
    }
}