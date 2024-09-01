package io.github.alphicc.android.largeSample.screens.bottomMenu

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.largeSampleRouter
import io.github.alphicc.android.largeSample.screens.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BottomMenuViewModel {

    private lateinit var firstMenuRouter: TreeRouter
    private lateinit var secondMenuRouter: TreeRouter
    private lateinit var thirdMenuRouter: TreeRouter

    val startScreenIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val containerRouter: MutableStateFlow<TreeRouter> = MutableStateFlow(TreeRouter.new())
    val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        viewModelScope.launch {
            firstMenuRouter = largeSampleRouter.branch(Screens.bottomMenuScreen.key).apply {
                addComponent(Screens.innerNavigationScreen, this)
            }
            containerRouter.value = firstMenuRouter
            startScreenIndex.value = 0
            secondMenuRouter = largeSampleRouter.branch(Screens.bottomMenuScreen.key).apply {
                addComponent(Screens.channelArgumentReceiveScreen)
            }
            thirdMenuRouter = largeSampleRouter.branch(Screens.bottomMenuScreen.key).apply {
                addComponent(Screens.channelArgumentSendScreen, this)
            }
        }
    }

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