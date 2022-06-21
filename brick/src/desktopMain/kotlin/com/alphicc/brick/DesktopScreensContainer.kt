package com.alphicc.brick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun DesktopScreensContainer(containerConnector: ContainerConnector, onRouterEmpty: () -> Unit = {}) {

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

    screen?.run {
        showContent(
            dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
            compositions = compositions
        )
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