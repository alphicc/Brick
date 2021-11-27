package com.navigationtestapp.viewModelSample.screens.someFirstDataDetails

import android.util.Log
import com.navigationtestapp.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens

class SomeFirstDataDetailsViewModel(private val router: TreeRouter) {

    init {
        Log.d("Alpha", "init SomeFirstDataDetailsViewModel")
    }

    fun onNextBtnClicked() {
        Log.d("Alpha", "onNextBtnClicked")
        router.addScreen(Screens.someFirstDataDetailsScreenTwo)
    }
}