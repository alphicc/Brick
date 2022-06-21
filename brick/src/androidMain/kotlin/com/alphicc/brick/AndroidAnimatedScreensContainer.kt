package com.alphicc.brick

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun AndroidAnimatedScreensContainer(
    containerConnector: ContainerConnector,
    onRouterEmpty: () -> Unit = {},
    enterTransition: EnterTransition = fadeIn(animationSpec = tween(300)),
    exitTransition: ExitTransition = fadeOut(animationSpec = tween(300))
) {

    val overlay by containerConnector.overlay.collectAsState()
    val screen by containerConnector.screen.collectAsState()
    val childList by containerConnector.childList.collectAsState()
    val isRouterEmpty by containerConnector.isRouterEmpty.collectAsState()
    val compositions by containerConnector.compositions.collectAsState()

    LaunchedEffect(isRouterEmpty) {
        if (isRouterEmpty) {
            onRouterEmpty.invoke()
        }
    }

    BackHandler(true) {
        containerConnector.onBackClicked()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = { enterTransition with exitTransition }
    ) {

        it?.run {
            showContent(
                dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
                compositions = compositions
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
        childScreen.showContent(
            dataContainer = childScreen.dependency ?: throw IllegalArgumentException("Dependency can not be null"),
            compositions = compositions
        )
    }

    overlay?.run {
        showContent(
            dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
            compositions = emptyMap()
        )
    }
}