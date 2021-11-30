package com.navigationtestapp.viewModelSample.screens.someFirstDataDetailsTwo

import android.util.Log
import com.navigationtestapp.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens

class SomeFirstDataDetailsViewModelTwo(private val router: TreeRouter) {

    init {
        Log.d("Alpha", "init SomeFirstDataDetailsViewModelTwo")
    }

    fun onBackClicked() {
        Log.d("Alpha", "onBackClicked")
        router.backScreen()
    }

    fun onForwardClicked() {
        Log.d("Alpha", "onForwardClicked")
        router.addChild(Screens.childInfoScreen)
    }

    fun onForwardTwoClicked() {
        Log.d("Alpha", "onForwardTwoClicked")
        router.addChild(Screens.childInfoScreenTwo)
    }
}