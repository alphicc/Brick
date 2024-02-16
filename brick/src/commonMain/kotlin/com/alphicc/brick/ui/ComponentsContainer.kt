package com.alphicc.brick.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.alphicc.brick.ui.gesture.BottomGestureScreen
import com.alphicc.brick.ui.gesture.TopGestureScreen
import com.alphicc.brick.ui.gesture.rememberSwipeGestureDescription

@Composable
fun ComponentsContainer(
//    containerConnector: ContainerConnector,
//    onRouterEmpty: () -> Unit = {},
//    enterTransition: EnterTransition = fadeIn(animationSpec = tween(300)),
//    exitTransition: ExitTransition = fadeOut(animationSpec = tween(300))
) {

    val swipeGestureDescription = rememberSwipeGestureDescription()

    BottomGestureScreen(swipeGestureDescription) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Red)
        )
    }

    TopGestureScreen(swipeGestureDescription) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Blue))
    }
//
//    val overlay by containerConnector.overlay.collectAsState()
//    val component by containerConnector.mainComponent.collectAsState()
//    val childList by containerConnector.childComponentsList.collectAsState()
//    val isRouterEmpty by containerConnector.isRouterEmpty.collectAsState()
//    val compositions by containerConnector.compositions.collectAsState()
//    val keepAliveComposeNodes by containerConnector.keepAliveNodes.collectAsState()
//
//    LaunchedEffect(isRouterEmpty) {
//        if (isRouterEmpty) {
//            onRouterEmpty.invoke()
//        }
//    }
//
//    BackHandler(true) {
//        containerConnector.onBackClicked()
//    }
//
//    keepAliveComposeNodes.forEach { keepAliveNode ->
//        keepAliveNode.mainComponent?.run {
//            showContent(
//                dataContainer = dependency
//                    ?: throw IllegalArgumentException("Dependency can not be null"),
//                compositions = keepAliveNode.compositions
//            )
//        }
//
//        keepAliveNode.childComponentsList.forEach { childScreen ->
//            childScreen.showContent(
//                dataContainer = childScreen.dependency
//                    ?: throw IllegalArgumentException("Dependency can not be null"),
//                compositions = keepAliveNode.compositions
//            )
//        }
//    }
//
//    if (component?.keepAliveCompose == false) {
//        AnimatedContent(
//            targetState = component,
//            transitionSpec = { enterTransition togetherWith exitTransition },
//            label = "NavigationTransitionAnim"
//        ) {
//
//            it?.run {
//                showContent(
//                    dataContainer = dependency
//                        ?: throw IllegalArgumentException("Dependency can not be null"),
//                    compositions = compositions
//                )
//            }
//
//            childList.forEach { childScreen ->
//                childScreen.showContent(
//                    dataContainer = childScreen.dependency
//                        ?: throw IllegalArgumentException("Dependency can not be null"),
//                    compositions = compositions
//                )
//            }
//
//            if (this.transition.isRunning) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clickable(
//                            interactionSource = remember { MutableInteractionSource() },
//                            indication = null,
//                            onClick = {})
//                )
//            }
//        }
//    }
//
//    overlay?.run {
//        showContent(
//            dataContainer = dependency
//                ?: throw IllegalArgumentException("Dependency can not be null"),
//            compositions = persistentMapOf()
//        )
//    }
}