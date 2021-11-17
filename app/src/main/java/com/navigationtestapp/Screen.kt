package com.navigationtestapp

import androidx.compose.runtime.Composable

class Screen(
    val key: String,
    val onCreate: () -> Unit = {},
    val onDestroy: () -> Unit = {},
    val content: @Composable () -> Unit
)