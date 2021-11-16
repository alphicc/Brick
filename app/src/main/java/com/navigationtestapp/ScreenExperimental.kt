package com.navigationtestapp

import androidx.compose.runtime.Composable

class Screen(
    val key: String,
    val content: @Composable () -> Unit
)

class ScreenExperimental(
    val key: String,
    val isContainer: Boolean = false,
    val content: @Composable () -> Unit
)

class ContainerScreen(
    val key: String,
    val content: @Composable () -> Unit
)