package io.github.alphicc.desktop.welcome

import com.alphicc.brick.TreeRouter
import io.github.alphicc.desktop.Screens

class WelcomeViewModel(private val treeRouter: TreeRouter) {

    fun onNextClicked() {
        treeRouter.addScreen(Screens.bottomMenuScreen)
    }
}