package io.github.alphicc.android.largeSample.screens.somethingDetails

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.screens.Screens

class SomethingDetailsViewModel(private val router: TreeRouter) {

    fun onNextBtnClicked() {
        router.addComponent(Screens.childNavigationContentScreen, router)
    }
}