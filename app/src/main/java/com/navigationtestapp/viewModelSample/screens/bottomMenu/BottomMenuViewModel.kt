package com.navigationtestapp.viewModelSample.screens.bottomMenu

import com.navigationtestapp.core.TreeRouter
import com.navigationtestapp.viewModelSample.screens.Screens
import com.navigationtestapp.viewModelSample.viewModelNavigationRouter
import kotlinx.coroutines.flow.MutableStateFlow

class BottomMenuViewModel {

    private val firstMenuRouter =
        viewModelNavigationRouter.branch("Menu1").apply {
            addScreen(Screens.menuFirstSubScreen, this)
        }
    private val secondMenuRouter =
        viewModelNavigationRouter.branch("Menu2").apply {
            addScreen(Screens.menuSecondSubScreen)
        }
    private val thirdMenuRouter =
        viewModelNavigationRouter.branch("Menu3").apply {
            addScreen(Screens.menuThirdSubScreen)
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