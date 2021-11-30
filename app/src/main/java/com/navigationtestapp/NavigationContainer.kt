package com.navigationtestapp

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun ScreensContainer(containerConnector: ContainerConnector) {

    val screen by containerConnector.screen.collectAsState()
    val childList by containerConnector.childList.collectAsState()

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
        }
    ) {

        it?.run {
            content.invoke(
                dependency ?: throw IllegalArgumentException("Dependency can not be null")
            )
        }

        if (this.transition.isRunning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {})
            )
        }
    }

    childList.forEach { childScreen ->
        childScreen.content.invoke(
            childScreen.dependency
                ?: throw IllegalArgumentException("Dependency can not be null")
        )
    }
    //childList.forEach { screen ->
    //    AnimatedContent(
    //        targetState = screen,
    //        transitionSpec = {
    //            fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
    //        }
    //    ) { childScreen ->
    //        childScreen.content.invoke(
    //            childScreen.dependency
    //                ?: throw IllegalArgumentException("Dependency can not be null")
    //        )
    //    }
    //}
}