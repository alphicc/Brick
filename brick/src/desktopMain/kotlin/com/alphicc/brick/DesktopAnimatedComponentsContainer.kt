package com.alphicc.brick

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.persistentMapOf

@ExperimentalAnimationApi
@Composable
fun DesktopAnimatedComponentsContainer(
    containerConnector: ContainerConnector,
    onRouterEmpty: () -> Unit = {},
    enterTransition: EnterTransition = fadeIn(animationSpec = tween(300)),
    exitTransition: ExitTransition = fadeOut(animationSpec = tween(300))
) {

    val overlay by containerConnector.overlay.collectAsState()
    val component by containerConnector.mainComponent.collectAsState()
    val childList by containerConnector.childComponentsList.collectAsState()
    val isRouterEmpty by containerConnector.isRouterEmpty.collectAsState()
    val compositions by containerConnector.compositions.collectAsState()
    val keepAliveComposeNodes by containerConnector.keepAliveNodes.collectAsState()

    LaunchedEffect(isRouterEmpty) {
        if (isRouterEmpty) {
            onRouterEmpty.invoke()
        }
    }

    keepAliveComposeNodes.forEach { keepAliveNode ->
        keepAliveNode.mainComponent?.run {
            showContent(
                dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
                compositions = keepAliveNode.compositions
            )
        }

        keepAliveNode.childComponentsList.forEach { childScreen ->
            childScreen.showContent(
                dataContainer = childScreen.dependency
                    ?: throw IllegalArgumentException("Dependency can not be null"),
                compositions = keepAliveNode.compositions
            )
        }
    }

    if (component?.keepAliveCompose == false) {
        AnimatedContent(
            targetState = component,
            transitionSpec = { enterTransition with exitTransition }
        ) {

            it?.run {
                showContent(
                    dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
                    compositions = compositions
                )
            }

            childList.forEach { childScreen ->
                childScreen.showContent(
                    dataContainer = childScreen.dependency
                        ?: throw IllegalArgumentException("Dependency can not be null"),
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
    }

    overlay?.run {
        showContent(
            dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
            compositions = persistentMapOf()
        )
    }
}