package io.github.alphicc.android.largeSample.screens.channelArgumentSendSample

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.screens.Screens
import kotlinx.coroutines.*

class ChannelArgumentSendViewModel(private val router: TreeRouter) {

    private var counter = 0

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun onIncrementClicked() {
        scope.launch {
            counter += 1
            router.passArgument(Screens.channelArgumentReceiveScreen.key, counter)
        }
    }

    fun onDestroy() {
        scope.cancel()
    }
}