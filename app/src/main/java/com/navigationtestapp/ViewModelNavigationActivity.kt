package com.navigationtestapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val rootRouter: TreeRouter = TreeRouterImpl()

private val bottomMenuScreen = Screen(
    key = "BottomMenuScreen",
    onCreate = { Log.d("Alpha", "onCreate bottomMenuScreen") },
    onDestroy = { Log.d("Alpha", "onDestroy bottomMenuScreen") },
    content = { BottomMenu() }
)

private val menu1SubScreen = Screen(
    key = "1",
    onCreate = { Log.d("Alpha", "onCreate menu1SubScreen") },
    onDestroy = { Log.d("Alpha", "onDestroy menu1SubScreen") },
    content = { SimpleScreen("SubScreen Menu1") }
)

private val menu2SubScreen = Screen(
    key = "2",
    onCreate = { Log.d("Alpha", "onCreate menu2SubScreen") },
    onDestroy = { Log.d("Alpha", "onDestroy menu2SubScreen") },
    content = { SimpleScreen("SubScreen Menu2") }
)

private val menu3SubScreen = Screen(
    key = "3",
    onCreate = { Log.d("Alpha", "onCreate menu3SubScreen") },
    onDestroy = { Log.d("Alpha", "onDestroy menu3SubScreen") },
    content = { SimpleScreen("SubScreen Menu3") }
)

class ViewModelNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreensContainer(rootRouter)
        }

        if (savedInstanceState == null) {
            rootRouter.addScreen(bottomMenuScreen)
        }
    }
}

@Composable
private fun BottomMenu() {
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .fillMaxSize()
        ) {
            InnerScreensContainer(rootRouter) {
                rootRouter.branch("Menu1", menu1SubScreen)
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(48.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { rootRouter.branch("Menu1", menu1SubScreen) }) {
                Text("Menu1")
            }

            Button(onClick = { rootRouter.branch("Menu2", menu2SubScreen) }) {
                Text("Menu2")
            }

            Button(onClick = { rootRouter.branch("Menu3", menu3SubScreen) }) {
                Text("Menu3")
            }
        }
    }
}

@Composable
private fun SimpleScreen(title: String) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title
        )
    }
}