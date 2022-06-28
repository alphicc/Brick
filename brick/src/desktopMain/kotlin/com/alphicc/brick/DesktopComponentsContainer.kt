package com.alphicc.brick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun DesktopComponentsContainer(containerConnector: ContainerConnector, onRouterEmpty: () -> Unit = {}) {

    val overlay by containerConnector.overlay.collectAsState()
    val component by containerConnector.mainComponent.collectAsState()
    val childList by containerConnector.childComponentsList.collectAsState()
    val isRouterEmpty by containerConnector.isRouterEmpty.collectAsState()
    val compositions by containerConnector.compositions.collectAsState()

    LaunchedEffect(isRouterEmpty) {
        if (isRouterEmpty) {
            onRouterEmpty.invoke()
        }
    }

    component?.run {
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