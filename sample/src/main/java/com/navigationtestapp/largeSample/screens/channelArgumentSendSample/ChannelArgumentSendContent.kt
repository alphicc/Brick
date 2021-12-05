package com.navigationtestapp.largeSample.screens.channelArgumentSendSample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChannelArgumentSendContent(title: String, onIncrementClicked: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title
        )

        Button(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 96.dp),
            onClick = onIncrementClicked
        ) {
            Text(text = "Increase")
        }
    }
}