package com.navigationtestapp.viewModelSample.screens.third

import android.util.Log
import com.navigationtestapp.core.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens
import kotlinx.coroutines.*

class ThirdContentViewModel(private val router: TreeRouter) {

    private var counter = 0

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun onIncrementClicked() {
        scope.launch {
            counter += 1
            router.passArgument(Screens.menuSecondSubScreen.key, counter)
        }
    }

    fun onDestroy() {
        Log.d("Alpha", "cancelScope Third")
        scope.cancel()
    }
}