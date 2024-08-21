package com.alphicc.brick.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun ComponentsContainer(
    containerConnector: ContainerConnector,
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