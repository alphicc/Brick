package com.navigationtestapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun ScreensContainer(containerConnector: ContainerConnector, onScreensEmpty: () -> Unit = {}) {

    val screenNode = containerConnector.node.collectAsState()

    screenNode.value?.let { node ->
        node.screen.content.invoke(
            node.screen.dependency ?: throw IllegalArgumentException("Dependency can not be null")
        )
        node.childScreens.forEach {
            it.content.invoke(
                it.dependency ?: throw IllegalArgumentException("Dependency can not be null")
            )
        }
    } ?: onScreensEmpty.invoke()
}