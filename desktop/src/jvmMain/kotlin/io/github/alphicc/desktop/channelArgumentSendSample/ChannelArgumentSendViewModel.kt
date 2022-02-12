package io.github.alphicc.desktop.channelArgumentSendSample

import com.alphicc.brick.TreeRouter
import io.github.alphicc.desktop.Screens
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