package com.navigationtestapp

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

class NavigationContainer(
    container: ComponentActivity,
    private val treeRouter: TreeRouter
) {

    init {
        container.setContent {
            BackHandler {
                val isLast = treeRouter.back()
                if (isLast) container.finish()
            }

            val screenNode = treeRouter.currentNode.collectAsState(initial = null)

            screenNode.value?.let { node ->
                ComposeContainer(node)
            }
        }
    }

    @Composable
    private fun ComposeContainer(node: Node) {
        if (node.router != null) {
            val innerNode = node.router.currentNode.collectAsState(initial = null)
            innerNode.value?.let { ComposeContainer(node = it) }
        }

        node.screen.content.invoke()
        node.childScreens.forEach {
            it.content.invoke()
        }
    }
}

//@Composable
//fun NavigationContainer(treeRouter: TreeRouter) {
//
//    val screenNode = treeRouter.currentNode.collectAsState(initial = null)
//
//    screenNode.value?.let { node ->
//        node.screen.content.invoke()
//        node.childScreens.forEach {
//            it.content.invoke()
//        }
//    }
//}