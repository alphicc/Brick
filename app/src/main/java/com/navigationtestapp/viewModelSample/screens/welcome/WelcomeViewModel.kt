package com.navigationtestapp.viewModelSample.screens.welcome

import com.navigationtestapp.core.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens

class WelcomeViewModel(private val treeRouter: TreeRouter) {

    fun onNextClicked() {
        treeRouter.addScreen(Screens.bottomMenuScreen)
    }
}