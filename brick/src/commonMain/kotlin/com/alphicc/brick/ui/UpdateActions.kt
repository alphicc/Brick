package com.alphicc.brick.ui

sealed class UpdateActions(val index: Int) {
    sealed class SwipeBack(index: Int) : UpdateActions(index) {
        data object Test : SwipeBack(1)
    }
}