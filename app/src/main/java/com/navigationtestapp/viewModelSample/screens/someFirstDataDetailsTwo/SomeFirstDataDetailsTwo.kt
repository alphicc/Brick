package com.navigationtestapp.viewModelSample.screens.someFirstDataDetailsTwo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SomeFirstDataDetailsTwo(viewModel: SomeFirstDataDetailsViewModelTwo) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "Inner navigation screen TWO")

        Button(onClick = viewModel::onBackClicked) {
            Text(text = "GO BACK")
        }

        Button(
            modifier = Modifier.align(alignment = Alignment.Center),
            onClick = viewModel::onForwardClicked
        ) {
            Text(text = "FORWARD CHILD")
        }
    }
}