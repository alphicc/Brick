package com.navigationtestapp.core

import com.navigationtestapp.*
import kotlinx.coroutines.flow.MutableStateFlow

class GraphController {

    private val keyManager = KeyManager()
    private val tree: ArrayList<Node> = ArrayList()

    val currentScreenFlow: MutableStateFlow<Screen<*>?> = MutableStateFlow(null)
    val currentChildFlow: MutableStateFlow<List<Screen<*>>> = MutableStateFlow(emptyList())

    private var currentNode: Node? = null

    fun <A> replaceScreen(screen: Screen<*>, argument: A?) {
        currentNode?.let { node -> keyManager.replaceKey(node.screen.key, screen.key) }

        currentNode?.screen?.let {
            val oldScreenLifecycleController = ScreenLifecycleController(it)
            oldScreenLifecycleController.onDestroy()
        }
        val newScreenLifecycleController = ScreenLifecycleController(screen)
        val newCreatedScreen = newScreenLifecycleController.onCreate(argument)
        currentNode?.screen = newCreatedScreen

        updateCurrentNode(currentNode)
    }

    fun <A> addScreen(screen: Screen<*>, argument: A?) {
        keyManager.add(screen.key)

        val screenLifecycleController = ScreenLifecycleController(screen)
        val createdScreen = screenLifecycleController.onCreate(argument)
        val node = Node(
            isActive = true,
            screen = createdScreen,
            childScreens = ArrayList(),
            neighbors = ArrayList(),
            parent = currentNode
        )
        tree.add(node)

        updateCurrentNode(node)
    }

    fun backChild() {
        currentNode?.run {
            if (childScreens.size >= 1) {
                val screen = childScreens.removeAt(childScreens.size - 1)
                keyManager.remove(screen.key)

                val screenLifecycleController = ScreenLifecycleController(screen)
                screenLifecycleController.onDestroy()

                updateCurrentNode(this)
            }
        }
    }

    fun backToChild(key: String) {
        currentNode?.run {
            val droppedChildList = dropChildUntilFoundKey(childScreens, key)

            droppedChildList.forEach {
                val screenLifecycleController = ScreenLifecycleController(it)
                screenLifecycleController.onDestroy()
            }

            updateCurrentNode(this)
        }
    }

    fun <A> replaceChild(screen: Screen<*>, argument: A?) {
        currentNode?.run {
            if (childScreens.size >= 1) {
                keyManager.replaceKey(childScreens.last().key, screen.key)

                val oldChildScreen = childScreens[childScreens.size - 1]
                val oldScreenLifecycleController = ScreenLifecycleController(oldChildScreen)
                oldScreenLifecycleController.onDestroy()

                val newScreenLifecycleController = ScreenLifecycleController(screen)
                childScreens[childScreens.size - 1] =
                    newScreenLifecycleController.onCreate(argument)

                updateCurrentNode(this)
            }
        }
    }

    fun <A> addChild(screen: Screen<*>, argument: A?) {
        currentNode?.run {
            keyManager.add(screen.key)

            val screenLifecycle = ScreenLifecycleController(screen)
            val createdScreen = screenLifecycle.onCreate(argument)
            childScreens.add(createdScreen)

            updateCurrentNode(this)
        }
    }

    private fun dropChildUntilFoundKey(
        childScreens: ArrayList<Screen<*>>,
        screenKey: String
    ): List<Screen<*>> {
        val droppedChildList = ArrayList<Screen<*>>()
        childScreens.lastOrNull()?.let {
            if (it.key != screenKey) {
                val element = childScreens.removeAt(childScreens.size - 1)
                droppedChildList.add(element)
                keyManager.remove(element.key)
                val innerDroppedChildList = dropChildUntilFoundKey(childScreens, screenKey)
                droppedChildList.addAll(innerDroppedChildList)
            } else return@let
        }
        return droppedChildList
    }

    private fun updateCurrentNode(node: Node?) {
        currentNode = node
        currentScreenFlow.value = node?.screen
        currentChildFlow.value = node?.childScreens?.toList() ?: emptyList()
    }

    //fun backScreen(): Screen<*> {
    //  //  currentNode?.isActive = false
    //    //  screen.onCreate?.invoke(screen.channel, this)
    //}

    //  fun backChild()

    // private fun cleanGraph(three: ArrayList<Node>): List<Node> {
    //     val droppedNodeList = ArrayList<Node>()
    //     three.removeAll { node ->
    //         if (!node.isActive) {
    //             keyManager.remove(node.screen.key)
    //             node.childScreens.forEach { childScreen -> keyManager.remove(childScreen.key) }
    //             droppedNodeList.add(node)
    //         }
    //         !node.isActive
    //     }
    //     three.forEach {
    //         val innerDroppedNodeList = cleanGraph(it.neighbors)
    //         droppedNodeList.addAll(innerDroppedNodeList)
    //     }
    //     return droppedNodeList
    // }
//
    // private fun startOnCreateLifecycle()
}