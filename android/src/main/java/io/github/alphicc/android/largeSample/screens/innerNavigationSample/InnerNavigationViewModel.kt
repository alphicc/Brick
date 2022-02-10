package io.github.alphicc.android.largeSample.screens.innerNavigationSample

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.screens.Screens

class InnerNavigationViewModel(private val router: TreeRouter) {

    fun onInnerNavigationClicked() {
        router.addScreen(Screens.somethingDetailsScreen, router)
    }
}