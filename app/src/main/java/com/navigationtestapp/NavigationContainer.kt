package com.navigationtestapp

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlin.system.exitProcess

class NavigationContainer(
    container: ComponentActivity,
    private val threeRouter: ThreeRouter
) {

    init {
        container.setContent {
            BackHandler {
                val isLast = threeRouter.back()
                if (isLast) container.finish()
            }

            val screenNode = threeRouter.currentNodeFlow.collectAsState(initial = null)

            screenNode.value?.let { node ->
                ComposeContainer(screen = node.screen, childScreenList = node.childScreens)
            }
        }
    }

    @Composable
    private fun ComposeContainer(screen: Screen, childScreenList: List<Screen>) {
        screen.content.invoke()
        childScreenList.forEach {
            it.content.invoke()
        }
    }
}