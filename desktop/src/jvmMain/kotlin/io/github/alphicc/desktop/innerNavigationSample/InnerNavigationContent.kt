package io.github.alphicc.desktop.innerNavigationSample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun InnerNavigationContent(onButtonClick: () -> Unit, title: String) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title
        )

        Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = onButtonClick) {
            Text(text = "OpenStackScreen")
        }
    }
}