package com.navigationtestapp.overlaySample.drawerScreen

import com.alphicc.brick.TreeRouter
import com.navigationtestapp.overlaySample.overlayScreen.overlayScreen
import kotlinx.coroutines.flow.MutableStateFlow

class DrawerViewModel(private val treeRouter: TreeRouter) {

    private val _isOpened: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isOpened: MutableStateFlow<Boolean> = _isOpened

    fun onCloseDrawer() {
        _isOpened.value = false
    }

    fun onDrawerClosed() {
        treeRouter.back()
    }

    fun onShowOverlayClicked() {
        treeRouter.setOverlay(overlayScreen, treeRouter)
    }
}