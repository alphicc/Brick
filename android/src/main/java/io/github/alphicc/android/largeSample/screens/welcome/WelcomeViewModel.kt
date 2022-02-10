package io.github.alphicc.android.largeSample.screens.welcome

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.screens.Screens

class WelcomeViewModel(private val treeRouter: TreeRouter) {

    fun onNextClicked() {
        treeRouter.addScreen(Screens.bottomMenuScreen)
    }
}