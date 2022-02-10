package com.alphicc.brick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ScreensContainer(containerConnector: ContainerConnector) {

    val overlay by containerConnector.overlay.collectAsState()
    val screen by containerConnector.screen.collectAsState()
    val childList by containerConnector.childList.collectAsState()

    screen?.run {
        content.invoke(
            dependency ?: throw IllegalArgumentException("Dependency can not be null")
        )
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