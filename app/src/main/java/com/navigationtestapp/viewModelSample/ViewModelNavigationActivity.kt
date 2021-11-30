package com.navigationtestapp.viewModelSample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.navigationtestapp.*
import com.navigationtestapp.viewModelSample.screens.Screens
import com.navigationtestapp.viewModelSample.screens.Screens.bottomMenuScreen

val viewModelNavigationRouter: TreeRouter = TreeRouterImpl()

@ExperimentalAnimationApi
class ViewModelNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreensContainer(viewModelNavigationRouter)
        }

        if (savedInstanceState == null) {
            viewModelNavigationRouter.addScreen(bottomMenuScreen)
        }
    }
}