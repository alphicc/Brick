package com.alphicc.brick.ui

import androidx.compose.runtime.Composable

@Composable
expect fun NativeBackHandler(enabled: Boolean, navigateBack: () -> Unit)