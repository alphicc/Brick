package io.github.alphicc.android.largeSample.screens.bottomMenu

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.largeSampleRouter
import io.github.alphicc.android.largeSample.screens.Screens
import kotlinx.coroutines.flow.MutableStateFlow

class BottomMenuViewModel {

    private val firstMenuRouter =
        largeSampleRouter.branch(Screens.bottomMenuScreen.key).apply {
            addScreen(Screens.innerNavigationScreen, this)
        }
    private val secondMenuRouter =
        largeSampleRouter.branch(Screens.bottomMenuScreen.key).apply {
            addScreen(Screens.channelArgumentReceiveScreen)
        }
    private val thirdMenuRouter =
        largeSampleRouter.branch(Screens.bottomMenuScreen.key).apply {
            addScreen(Screens.channelArgumentSendScreen, this)
        }

    val startScreenIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val containerRouter: MutableStateFlow<TreeRouter> = MutableStateFlow(firstMenuRouter)

    fun onFirstMenuClicked() {
        containerRouter.value = firstMenuRouter
        startScreenIndex.value = 0
    }

    fun onSecondMenuClicked() {
        containerRouter.value = secondMenuRouter
        startScreenIndex.value = 1
    }

    fun onThirdMenuClicked() {
        containerRouter.value = thirdMenuRouter
        startScreenIndex.value = 2
    }
}