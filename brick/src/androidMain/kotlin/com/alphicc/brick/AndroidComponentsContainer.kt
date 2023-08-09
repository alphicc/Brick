package com.alphicc.brick

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AndroidComponentsContainer(containerConnector: ContainerConnector, onRouterEmpty: () -> Unit = {}) {

    val overlay by containerConnector.overlay.collectAsState()
    val component by containerConnector.mainComponent.collectAsState()
    val childList by containerConnector.childComponentsList.collectAsState()
    val isRouterEmpty by containerConnector.isRouterEmpty.collectAsState()
    val compositions by containerConnector.compositions.collectAsState()
    val keepAliveNodes by containerConnector.keepAliveNodes.collectAsState()

    LaunchedEffect(isRouterEmpty) {
        if (isRouterEmpty) {
            onRouterEmpty.invoke()
        }
    }

    BackHandler(true) {
        containerConnector.onBackClicked()
    }

    keepAliveNodes.forEach { keepAliveNode ->
        keepAliveNode.mainComponent?.run {
            showContent(
                dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
                compositions = keepAliveNode.compositions
            )
        }
        keepAliveNode.childComponentsList.forEach { childScreen ->
            childScreen.showContent(
                dataContainer = childScreen.dependency ?: throw IllegalArgumentException("Dependency can not be null"),
                compositions = keepAliveNode.compositions
            )
        }
    }

    if (component?.keepAliveCompose == false) {
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
    }
    overlay?.run {
        showContent(
            dataContainer = dependency ?: throw IllegalArgumentException("Dependency can not be null"),
            compositions = emptyMap()
        )
    }
}