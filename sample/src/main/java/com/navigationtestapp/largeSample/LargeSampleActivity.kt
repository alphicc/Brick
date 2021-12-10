package com.navigationtestapp.largeSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import com.alphicc.brick.AnimatedScreensContainer
import com.alphicc.brick.TreeRouter
import com.navigationtestapp.largeSample.screens.Screens.welcomeScreen

val largeSampleRouter: TreeRouter = TreeRouter.new()

@ExperimentalAnimationApi
class LargeSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnimatedScreensContainer(
                containerConnector = largeSampleRouter,
                enterTransition = scaleIn(
                    initialScale = .90f,
                    animationSpec = tween(easing = LinearEasing)
                ) + fadeIn(),
                exitTransition = scaleOut(
                    targetScale = .90f,
                    animationSpec = tween(easing = LinearEasing)
                ) + fadeOut(),
            )
        }

        if (savedInstanceState == null) {
            largeSampleRouter.addScreen(welcomeScreen, largeSampleRouter)
        }
    }
}