package com.navigationtestapp.viewModelSample.screens.welcome

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun Welcome(
    onNextClicked: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Button(modifier = Modifier.align(Alignment.Center), onClick = onNextClicked) {
            Text(text = "Open Bottom Menu")
        }
    }
}