package com.alphicc.brick.navigationContainers

import androidx.activity.compose.BackHandler
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
import com.alphicc.brick.ContainerConnector

@ExperimentalAnimationApi
@Composable
fun AnimatedScreensContainer(
    containerConnector: ContainerConnector,
    enterTransition: EnterTransition = fadeIn(animationSpec = tween(300)),
    exitTransition: ExitTransition = fadeOut(animationSpec = tween(300))
) {

    val overlay by containerConnector.overlay.collectAsState()
    val screen by containerConnector.screen.collectAsState()
    val childList by containerConnector.childList.collectAsState()
    val hasBackNavigationVariants by containerConnector.hasBackNavigationVariants.collectAsState()

    BackHandler(hasBackNavigationVariants) {
        containerConnector.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = { enterTransition with exitTransition }
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

    overlay?.run {
        content.invoke(
            dependency ?: throw IllegalArgumentException("Dependency can not be null")
        )
    }
}