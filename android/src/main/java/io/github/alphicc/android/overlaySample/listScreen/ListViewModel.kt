package io.github.alphicc.android.overlaySample.listScreen

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.overlaySample.drawerScreen.drawerScreen

class ListViewModel(private val router: TreeRouter) {

    fun onOpenNavigationDrawerClicked() {
        router.addChild(drawerScreen, router)
    }
}