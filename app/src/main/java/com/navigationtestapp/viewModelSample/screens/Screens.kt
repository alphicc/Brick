package com.navigationtestapp.viewModelSample.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.navigationtestapp.core.Screen
import com.navigationtestapp.core.TreeRouter
import com.navigationtestapp.viewModelSample.screens.bottomMenu.BottomMenuScreen
import com.navigationtestapp.viewModelSample.screens.bottomMenu.BottomMenuViewModel
import com.navigationtestapp.viewModelSample.screens.childInfoOne.ChildInfoOneScreen
import com.navigationtestapp.viewModelSample.screens.childInfoOne.ChildInfoOneViewModel
import com.navigationtestapp.viewModelSample.screens.childInfoTwo.ChildInfoTwoScreen
import com.navigationtestapp.viewModelSample.screens.childInfoTwo.ChildInfoTwoViewModel
import com.navigationtestapp.viewModelSample.screens.first.FirstContent
import com.navigationtestapp.viewModelSample.screens.first.FirstContentViewModel
import com.navigationtestapp.viewModelSample.screens.second.SecondContent
import com.navigationtestapp.viewModelSample.screens.second.SecondContentViewModel
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetails.SomeFirstDataDetails
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetails.SomeFirstDataDetailsViewModel
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetailsTwo.SomeFirstDataDetailsTwo
import com.navigationtestapp.viewModelSample.screens.someFirstDataDetailsTwo.SomeFirstDataDetailsViewModelTwo
import com.navigationtestapp.viewModelSample.screens.third.ThirdContent
import com.navigationtestapp.viewModelSample.screens.third.ThirdContentViewModel
import com.navigationtestapp.viewModelSample.screens.welcome.Welcome
import com.navigationtestapp.viewModelSample.screens.welcome.WelcomeViewModel

@OptIn(ExperimentalAnimationApi::class)
object Screens {

    val welcomeScreen = Screen(
        key = "WelcomeScreen",
        onCreate = { _, argument ->
            Log.d("Alpha", "onCreate WelcomeScreen")
            val router = argument.get<TreeRouter>()
            return@Screen WelcomeViewModel(router)
        },
        onDestroy = { Log.d("Alpha", "onDestroy WelcomeScreen") },
        content = {
            val viewModel = it.get<WelcomeViewModel>()
            Welcome(viewModel::onNextClicked)
        }
    )

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
        onCreate = { _, argument ->
            Log.d("Alpha", "onCreate menu1SubScreen")
            val router = argument.get<TreeRouter>()
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
        onCreate = { channel, _ -> return@Screen SecondContentViewModel(channel) },
        onDestroy = {
            val viewModel = it.get<SecondContentViewModel>()
            viewModel.onDestroy()
        },
        content = {
            val secondContentViewModel = it.get<SecondContentViewModel>()
            val count by secondContentViewModel.count.collectAsState()
            SecondContent(count)
        }
    )

    val menuThirdSubScreen = Screen(
        key = "menuThirdSubScreen",
        onCreate = { _, argument ->
            Log.d("Alpha", "onCreate menu3SubScreen")
            val router = argument.get<TreeRouter>()
            ThirdContentViewModel(router)
        },
        onDestroy = {
            val viewModel = it.get<ThirdContentViewModel>()
            viewModel.onDestroy()
            Log.d("Alpha", "onDestroy menu3SubScreen")
        },
        content = {
            val viewModel = it.get<ThirdContentViewModel>()
            ThirdContent("SubScreen Menu3", viewModel::onIncrementClicked)
        }
    )

    val someFirstDataDetailsScreen = Screen(
        key = "someFirstDataDetailsScreen",
        onCreate = { _, argument ->
            Log.d("Alpha", "onCreate SomeFirstDataDetails")
            val router = argument.get<TreeRouter>()
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
        onCreate = { _, argument ->
            Log.d("Alpha", "onCreate SomeFirstDataDetailsTwo")
            val router = argument.get<TreeRouter>()
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
        onCreate = { _, argument ->
            Log.d("Alpha", "onCreate ChildInfoScreen")
            val router = argument.get<TreeRouter>()
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
        onCreate = { _, argument ->
            Log.d("Alpha", "onCreate ChildInfoScreenTwo")
            val router = argument.get<TreeRouter>()
            ChildInfoTwoViewModel(router)
        },
        onDestroy = { Log.d("Alpha", "onDestroy ChildInfoScreenTwo") },
        content = {
            val viewModel = it.get<ChildInfoTwoViewModel>()
            ChildInfoTwoScreen(viewModel)
        }
    )
}