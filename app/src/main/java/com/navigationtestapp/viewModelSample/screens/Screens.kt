package com.navigationtestapp.viewModelSample.screens

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.navigationtestapp.Screen
import com.navigationtestapp.viewModelSample.screens.bottomMenu.BottomMenuScreen
import com.navigationtestapp.viewModelSample.screens.bottomMenu.BottomMenuViewModel
import com.navigationtestapp.viewModelSample.screens.childInfoOne.ChildInfoOneScreen
import com.navigationtestapp.viewModelSample.screens.childInfoOne.ChildInfoOneViewModel
import com.navigationtestapp.viewModelSample.screens.childInfoTwo.ChildInfoTwoScreen
import com.navigationtestapp.viewModelSample.screens.childInfoTwo.ChildInfoTwoViewModel
import com.navigationtestapp.viewModelSample.screens.first.FirstContent
import com.navigationtestapp.viewModelSample.screens.first.FirstContentViewModel
import com.navigationtestapp.viewModelSample.screens.second.SecondContent
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetails.SomeFirstDataDetails
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetails.SomeFirstDataDetailsViewModel
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetailsTwo.SomeFirstDataDetailsTwo
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetailsTwo.SomeFirstDataDetailsViewModelTwo
import com.navigationtestapp.viewModelSample.screens.third.ThirdContent

object Screens {

    val bottomMenuScreen = Screen(
        key = "BottomMenuScreen",
        onCreate = { _, _ ->
            Log.d("Alpha", "onCreate bottomMenuScreen")
            return@Screen BottomMenuViewModel()
        },
        onDestroy = { Log.d("Alpha", "onDestroy bottomMenuScreen") },
        content = {
            val viewModel = it.get<BottomMenuViewModel>()
            val defaultIndex by viewModel.startScreenIndex.collectAsState()
            val containerRouter by viewModel.containerRouter.collectAsState()

            BottomMenuScreen(
                defaultIndex,
                containerRouter,
                viewModel::onFirstMenuClicked,
                viewModel::onSecondMenuClicked,
                viewModel::onThirdMenuClicked
            )
        }
    )

    val menuFirstSubScreen = Screen(
        key = "menuFirstSubScreen",
        onCreate = { _, router ->
            Log.d("Alpha", "onCreate menu1SubScreen")
            FirstContentViewModel(router)
        },
        onDestroy = { Log.d("Alpha", "onDestroy menu1SubScreen") },
        content = {
            val viewModel = it.get<FirstContentViewModel>()
            FirstContent(viewModel::onInnerNavigationClicked, "SubScreen Menu1")
        }
    )

    val menuSecondSubScreen = Screen(
        key = "menuSecondSubScreen",
        onCreate = { _, _ -> Log.d("Alpha", "onCreate menu2SubScreen") },
        onDestroy = { Log.d("Alpha", "onDestroy menu2SubScreen") },
        content = { SecondContent("SubScreen Menu2") }
    )

    val menuThirdSubScreen = Screen(
        key = "menuThirdSubScreen",
        onCreate = { _, _ -> Log.d("Alpha", "onCreate menu3SubScreen") },
        onDestroy = { Log.d("Alpha", "onDestroy menu3SubScreen") },
        content = { ThirdContent("SubScreen Menu3") }
    )

    val someFirstDataDetailsScreen = Screen(
        key = "someFirstDataDetailsScreen",
        onCreate = { _, router ->
            Log.d("Alpha", "onCreate SomeFirstDataDetails")
            SomeFirstDataDetailsViewModel(router)
        },
        onDestroy = { Log.d("Alpha", "onDestroy SomeFirstDataDetails") },
        content = {
            val viewModel = it.get<SomeFirstDataDetailsViewModel>()
            SomeFirstDataDetails(viewModel)
        }
    )

    val someFirstDataDetailsScreenTwo = Screen(
        key = "someFirstDataDetailsScreenTwo",
        onCreate = { _, router ->
            Log.d("Alpha", "onCreate SomeFirstDataDetailsTwo")
            SomeFirstDataDetailsViewModelTwo(router)
        },
        onDestroy = { Log.d("Alpha", "onDestroy SomeFirstDataDetailsTwo") },
        content = {
            val viewModel = it.get<SomeFirstDataDetailsViewModelTwo>()
            SomeFirstDataDetailsTwo(viewModel)
        }
    )

    val childInfoScreen = Screen(
        key = "ChildInfoScreen",
        onCreate = { _, router ->
            Log.d("Alpha", "onCreate ChildInfoScreen")
            ChildInfoOneViewModel(router)
        },
        onDestroy = { Log.d("Alpha", "onDestroy ChildInfoScreen") },
        content = {
            val viewModel = it.get<ChildInfoOneViewModel>()
            ChildInfoOneScreen(viewModel)
        }
    )

    val childInfoScreenTwo = Screen(
        key = "ChildInfoScreenTwo",
        onCreate = { _, router ->
            Log.d("Alpha", "onCreate ChildInfoScreen")
            ChildInfoTwoViewModel(router)
        },
        onDestroy = { Log.d("Alpha", "onDestroy ChildInfoScreen") },
        content = {
            val viewModel = it.get<ChildInfoTwoViewModel>()
            ChildInfoTwoScreen(viewModel)
        }
    )
}