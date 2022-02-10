package io.github.alphicc.android.largeSample.screens.childNavigationSample

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.screens.Screens

class ChildNavigationViewModel(private val router: TreeRouter) {

    fun onBackClicked() {
        router.backScreen()
    }

    fun onForwardClicked() {
        router.addChild(Screens.redSquareScreen, router)
    }

    fun onForwardTwoClicked() {
        router.addChild(Screens.blueSquareScreen, router)
    }
}