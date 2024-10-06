package com.alphicc.brick.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.persistentMapOf

private const val DEFAULT_ANIM_FRAME_RATE = 8

@Composable
fun ComponentsContainer(
    containerConnector: ContainerConnector,
    backgroundColor: Color = Color.White,
    frameRate: Int = DEFAULT_ANIM_FRAME_RATE,
    onRouterEmpty: () -> Unit = {}
) {

    val overlay by containerConnector.overlay.collectAsState()

    val nodes by containerConnector.nodes.collectAsState()

    val isRouterEmpty by containerConnector.isRouterEmpty.collectAsState()
    val animState by containerConnector.animState.collectAsState()

    LaunchedEffect(isRouterEmpty) {
        if (isRouterEmpty) {
            onRouterEmpty.invoke()
        }
    }

    RootAnimator(
        nodes = nodes,
        animState = animState,
        frameRate = frameRate,
        backgroundColor = backgroundColor,
        navigateBack = { immediately: Boolean ->
            containerConnector.onBackClicked(immediately)
        }
    )

    overlay?.run {
        showContent(
            dataContainer = dependency
                ?: throw IllegalArgumentException("Dependency can not be null"),
            compositions = persistentMapOf()
        )
    }
}