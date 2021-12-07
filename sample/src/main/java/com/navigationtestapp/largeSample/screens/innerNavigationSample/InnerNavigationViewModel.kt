package com.navigationtestapp.largeSample.screens.innerNavigationSample

import com.alphicc.brick.TreeRouter
import com.navigationtestapp.largeSample.screens.Screens

class InnerNavigationViewModel(private val router: TreeRouter) {

    fun onInnerNavigationClicked() {
        router.addScreen(Screens.somethingDetailsScreen, router)
    }
}