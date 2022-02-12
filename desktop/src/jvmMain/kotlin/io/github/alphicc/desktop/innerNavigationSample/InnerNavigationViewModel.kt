package io.github.alphicc.desktop.innerNavigationSample

import com.alphicc.brick.TreeRouter
import io.github.alphicc.desktop.Screens

class InnerNavigationViewModel(private val router: TreeRouter) {

    fun onInnerNavigationClicked() {
        router.addScreen(Screens.somethingDetailsScreen, router)
    }
}