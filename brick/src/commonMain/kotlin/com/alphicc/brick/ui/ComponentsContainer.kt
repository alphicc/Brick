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

    val component by containerConnector.mainComponent.collectAsState()
    val childList by containerConnector.childComponents.collectAsState()
    val compositions by containerConnector.compositions.collectAsState()

    val keepAliveNodes by containerConnector.keepAliveNodes.collectAsState()

    val isRouterEmpty by containerConnector.isRouterEmpty.collectAsState()

    LaunchedEffect(isRouterEmpty) {
        if (isRouterEmpty) {
            onRouterEmpty.invoke()
        }
    }

    NativeBackHandler(containerConnector)

    RootAnimator(keepAliveNodes)

    component?.run {
        showContent(
            dataContainer = dependency
                ?: throw IllegalArgumentException("Dependency can not be null"),
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

    overlay?.run {
        showContent(
            dataContainer = dependency
                ?: throw IllegalArgumentException("Dependency can not be null"),
            compositions = persistentMapOf()
        )
    }
}