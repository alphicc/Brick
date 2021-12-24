package com.navigationtestapp.overlaySample.overlayScreen

import com.alphicc.brick.TreeRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OverlayViewModel(private val treeRouter: TreeRouter) {

    private val _isVisible: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isVisible: StateFlow<Boolean> = _isVisible

    fun onRemoveOverlayClicked() {
        _isVisible.value = false
    }

    fun onOverlayHidden() {
        treeRouter.removeOverlay()
    }
}