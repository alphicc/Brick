package io.github.alphicc.desktop.channelArgumentReceiveSample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ChannelArgumentReceiveContent(count: Int) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "COUNT $count"
        )
    }
}