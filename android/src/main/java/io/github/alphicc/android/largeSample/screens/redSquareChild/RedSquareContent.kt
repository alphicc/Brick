package io.github.alphicc.android.largeSample.screens.redSquareChild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RedSquareContent(oneViewModel: RedSquareViewModel) {

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(64.dp)
                .background(Color.Red)
        ) {
            Text(modifier = Modifier.align(Alignment.Center), text = "Child Screen")
        }
    }
}