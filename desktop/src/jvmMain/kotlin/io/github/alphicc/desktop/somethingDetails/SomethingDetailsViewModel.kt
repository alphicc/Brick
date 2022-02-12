package io.github.alphicc.desktop.somethingDetails

import com.alphicc.brick.TreeRouter
import io.github.alphicc.desktop.Screens

class SomethingDetailsViewModel(private val router: TreeRouter) {

    fun onNextBtnClicked() {
        router.addScreen(Screens.childNavigationContentScreen, router)
    }
}