package com.navigationtestapp.viewModelSample.screens.someFirstDataDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SomeFirstDataDetails(viewModel: SomeFirstDataDetailsViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "Inner navigation screen")

        Button(onClick = viewModel::onNextBtnClicked) {
            Text(text = "FORWARD")
        }
    }
}