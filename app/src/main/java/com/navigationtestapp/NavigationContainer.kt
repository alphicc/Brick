package com.navigationtestapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun ScreensContainer(containerConnector: ContainerConnector, onScreensEmpty: () -> Unit = {}) {

    val screenNode = containerConnector.currentNode.collectAsState()

    screenNode.value?.let { node ->
        node.screen.content.invoke()
        node.childScreens.forEach {
            it.content.invoke()
        }
    } ?: onScreensEmpty.invoke()
}

@Composable
fun InnerScreensContainer(
    containerConnector: ContainerConnector,
    onScreensEmpty: () -> Unit = {}
) {

    val innerNodeConnector = containerConnector.innerNodeConnector.collectAsState()
    val screenNode = innerNodeConnector.value?.currentNode?.collectAsState()

    screenNode?.value?.let { node ->
        node.screen.content.invoke()
        node.childScreens.forEach {
            it.content.invoke()
        }
    } ?: onScreensEmpty.invoke()
}


//@Composable
//fun NestedContainer(treeRouter: TreeRouter, startNavigation: () -> Unit = {}) {
//
//    val router = treeRouter.currentChildRouter.collectAsState()
//    val screenNode = router.value?.currentNode?.collectAsState()
//
//    screenNode?.value?.let { node ->
//        node.screen.content.invoke()
//        node.childScreens.forEach {
//            it.content.invoke()
//        }
//    } ?: startNavigation.invoke()
//}