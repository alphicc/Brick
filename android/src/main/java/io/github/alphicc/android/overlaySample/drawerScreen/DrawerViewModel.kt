package io.github.alphicc.android.overlaySample.drawerScreen

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.overlaySample.overlayScreen.overlayScreen
import kotlinx.coroutines.flow.MutableStateFlow

class DrawerViewModel(private val treeRouter: TreeRouter) {

    private val _isOpened: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isOpened: MutableStateFlow<Boolean> = _isOpened

    fun onCloseDrawer() {
        _isOpened.value = false
    }

    fun onDrawerClosed() {
        treeRouter.onBackClicked()
    }

    fun onShowOverlayClicked() {
        treeRouter.setOverlay(overlayScreen, treeRouter)
    }
}