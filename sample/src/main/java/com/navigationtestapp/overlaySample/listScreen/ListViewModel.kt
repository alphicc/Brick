package com.navigationtestapp.overlaySample.listScreen

import com.alphicc.brick.TreeRouter
import com.navigationtestapp.overlaySample.drawerScreen.drawerScreen

class ListViewModel(private val router: TreeRouter) {

    fun onOpenNavigationDrawerClicked() {
        router.addChild(drawerScreen, router)
    }
}