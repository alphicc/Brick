package com.navigationtestapp.viewModelSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.navigationtestapp.core.AnimatedScreensContainer
import com.navigationtestapp.core.TreeRouter
import com.navigationtestapp.core.TreeRouterImpl
import com.navigationtestapp.viewModelSample.screens.Screens.bottomMenuScreen

val viewModelNavigationRouter: TreeRouter = TreeRouterImpl()

@ExperimentalAnimationApi
class ViewModelNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnimatedScreensContainer(viewModelNavigationRouter)
        }

        if (savedInstanceState == null) {
            viewModelNavigationRouter.addScreen(bottomMenuScreen)
        }
    }
}