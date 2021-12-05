package com.navigationtestapp.largeSample.screens.childNavigationSample

import com.alphicc.brick.TreeRouter
import com.navigationtestapp.largeSample.screens.Screens

class ChildNavigationViewModel(private val router: TreeRouter) {

    fun onBackClicked() {
        router.backScreen()
    }

    fun onForwardClicked() {
        router.addChild(Screens.childInfoScreen, router)
    }

    fun onForwardTwoClicked() {
        router.addChild(Screens.childInfoScreenTwo, router)
    }
}