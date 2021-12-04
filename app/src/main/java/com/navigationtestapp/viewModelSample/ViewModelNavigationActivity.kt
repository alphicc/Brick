package com.navigationtestapp.viewModelSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.alphicc.brick.AnimatedScreensContainer
import com.alphicc.brick.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens.welcomeScreen

val viewModelNavigationRouter: TreeRouter = TreeRouter.new()

@ExperimentalAnimationApi
class ViewModelNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnimatedScreensContainer(viewModelNavigationRouter)
        }

        if (savedInstanceState == null) {
            viewModelNavigationRouter.addScreen(welcomeScreen, viewModelNavigationRouter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModelNavigationRouter.cleanRouter()
    }
}