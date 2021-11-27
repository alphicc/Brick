package com.navigationtestapp.viewModelSample.screens.first

import android.util.Log
import com.navigationtestapp.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens

class FirstContentViewModel(private val router: TreeRouter) {

    init {
        Log.d("Alpha", "init FirstContentViewModel")
    }

    fun onInnerNavigationClicked() {
        Log.d("Alpha", "onInnerNavigationClicked")
        router.addScreen(Screens.someFirstDataDetailsScreen)
    }
}