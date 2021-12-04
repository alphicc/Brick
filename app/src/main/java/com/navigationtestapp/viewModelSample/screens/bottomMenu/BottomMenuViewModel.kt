package com.navigationtestapp.viewModelSample.screens.bottomMenu

import com.alphicc.brick.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens
import com.navigationtestapp.viewModelSample.viewModelNavigationRouter
import kotlinx.coroutines.flow.MutableStateFlow

class BottomMenuViewModel {

    private val firstMenuRouter =
        viewModelNavigationRouter.branch(Screens.bottomMenuScreen.key).apply {
            addScreen(Screens.menuFirstSubScreen, this)
        }
    private val secondMenuRouter =
        viewModelNavigationRouter.branch(Screens.bottomMenuScreen.key).apply {
            addScreen(Screens.menuSecondSubScreen)
        }
    private val thirdMenuRouter =
        viewModelNavigationRouter.branch(Screens.bottomMenuScreen.key).apply {
            addScreen(Screens.menuThirdSubScreen, this)
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