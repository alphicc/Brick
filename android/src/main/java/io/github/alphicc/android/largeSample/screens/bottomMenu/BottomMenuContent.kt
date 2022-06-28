package io.github.alphicc.android.largeSample.screens.bottomMenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alphicc.brick.AndroidAnimatedComponentsContainer
import com.alphicc.brick.TreeRouter

@ExperimentalAnimationApi
@Composable
fun BottomMenuScreen(
    defaultIndex: Int,
    containerRouter: TreeRouter,
    onFirstMenuClicked: () -> Unit,
    onSecondMenuClicked: () -> Unit,
    onThirdMenuClicked: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .fillMaxSize()
        ) {
            AndroidAnimatedComponentsContainer(containerRouter)
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(48.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onFirstMenuClicked) {
                Text(text = "Menu1", color = if (defaultIndex == 0) Color.Black else Color.Gray)
            }

            Button(onClick = onSecondMenuClicked) {
                Text(text = "Menu2", color = if (defaultIndex == 1) Color.Black else Color.Gray)
            }

            Button(onClick = onThirdMenuClicked) {
                Text(text = "Menu3", color = if (defaultIndex == 2) Color.Black else Color.Gray)
            }
        }
    }
}