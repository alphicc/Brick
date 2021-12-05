package com.navigationtestapp.largeSample.screens.somethingDetails

import com.alphicc.brick.TreeRouter
import com.navigationtestapp.largeSample.screens.Screens

class SomethingDetailsViewModel(private val router: TreeRouter) {

    fun onNextBtnClicked() {
        router.addScreen(Screens.childNavigationContentScreen, router)
    }
}