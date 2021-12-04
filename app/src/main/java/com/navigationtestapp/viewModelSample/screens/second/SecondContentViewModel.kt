package com.navigationtestapp.viewModelSample.screens.second

import android.util.Log
import com.alphicc.brick.DataContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SecondContentViewModel(channel: SharedFlow<DataContainer>) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _count: MutableStateFlow<Int> = MutableStateFlow(0)
    val count: MutableStateFlow<Int> = _count

    init {
        channel.onEach { _count.value = it.get() }.launchIn(scope)
    }

    fun onDestroy() {
        Log.d("Alpha", "cancelScope Second")
        scope.cancel()
    }
}