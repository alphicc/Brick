package com.alphicc.brick

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AndroidScreensContainer(containerConnector: ContainerConnector) {

    val overlay by containerConnector.overlay.collectAsState()
    val screen by containerConnector.screen.collectAsState()
    val childList by containerConnector.childList.collectAsState()
    val hasBackNavigationVariants by containerConnector.hasBackNavigationVariants.collectAsState()

    BackHandler(hasBackNavigationVariants) {
        containerConnector.back()
    }

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