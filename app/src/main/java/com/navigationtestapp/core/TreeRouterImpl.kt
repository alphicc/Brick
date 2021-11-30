package com.navigationtestapp.core

import com.navigationtestapp.Screen
import com.navigationtestapp.TreeRouter
import kotlinx.coroutines.flow.StateFlow

class TreeRouterImpl(
    override val initialScreen: Screen<*>? = null,
    override val parentRouter: TreeRouter? = null
) : TreeRouter {

    override val screen: StateFlow<Screen<*>?>
        get() = TODO("Not yet implemented")
    override val childList: StateFlow<List<Screen<*>>>
        get() = TODO("Not yet implemented")
    private val graphController: GraphController = GraphController()

    override fun back(): Boolean {
        TODO("Not yet implemented")
    }

    override fun branch(key: String, initialScreen: Screen<*>?): TreeRouter {
        TODO("Not yet implemented")
    }

    override fun backScreen() {
        TODO("Not yet implemented")
    }

    override fun backToScreen(key: String) {
        TODO("Not yet implemented")
    }

    override fun replaceScreen(screen: Screen<*>) {
        TODO("Not yet implemented")
    }

    override fun <A> replaceScreen(screen: Screen<*>, argument: A) {
        TODO("Not yet implemented")
    }

    override fun addScreen(screen: Screen<*>) =
        graphController.addScreen(screen, null)

    override fun <A> addScreen(screen: Screen<*>, argument: A) =
        graphController.addScreen(screen, argument)

    override fun backChild() = graphController.backChild()

    override fun backToChild(key: String) = graphController.backToChild(key)

    override fun replaceChild(screen: Screen<*>) =
        graphController.replaceChild(screen, null)

    override fun <A> replaceChild(screen: Screen<*>, argument: A) =
        graphController.replaceChild(screen, argument)

    override fun addChild(screen: Screen<*>) =
        graphController.addChild(screen, null)

    override fun <A> addChild(screen: Screen<*>, argument: A) =
        graphController.addChild(screen, argument)
}