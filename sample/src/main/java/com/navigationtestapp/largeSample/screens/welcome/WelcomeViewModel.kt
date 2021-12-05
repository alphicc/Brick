package com.navigationtestapp.largeSample.screens.welcome

import com.alphicc.brick.TreeRouter
import com.navigationtestapp.largeSample.screens.Screens

class WelcomeViewModel(private val treeRouter: TreeRouter) {

    fun onNextClicked() {
        treeRouter.addScreen(Screens.bottomMenuScreen)
    }
}