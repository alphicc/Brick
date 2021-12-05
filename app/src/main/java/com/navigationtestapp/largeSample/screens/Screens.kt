package com.navigationtestapp.largeSample.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.navigationtestapp.largeSample.screens.blueSquareChild.ChildInfoTwoViewModel
import com.navigationtestapp.largeSample.screens.blueSquareChild.RedSquareContent
import com.navigationtestapp.largeSample.screens.bottomMenu.BottomMenuScreen
import com.navigationtestapp.largeSample.screens.bottomMenu.BottomMenuViewModel
import com.navigationtestapp.largeSample.screens.channelArgumentReceiveSample.ChannelArgumentReceiveContent
import com.navigationtestapp.largeSample.screens.channelArgumentReceiveSample.ChannelArgumentReceiveViewModel
import com.navigationtestapp.largeSample.screens.channelArgumentSendSample.ChannelArgumentSendContent
import com.navigationtestapp.largeSample.screens.channelArgumentSendSample.ChannelArgumentSendViewModel
import com.navigationtestapp.largeSample.screens.childNavigationSample.ChildNavigationContent
import com.navigationtestapp.largeSample.screens.childNavigationSample.ChildNavigationViewModel
import com.navigationtestapp.largeSample.screens.innerNavigationSample.InnerNavigationContent
import com.navigationtestapp.largeSample.screens.innerNavigationSample.InnerNavigationViewModel
import com.navigationtestapp.largeSample.screens.redSquareChild.RedSquareContent
import com.navigationtestapp.largeSample.screens.redSquareChild.RedSquareViewModel
import com.navigationtestapp.largeSample.screens.somethingDetails.SomethingDetailsContent
import com.navigationtestapp.largeSample.screens.somethingDetails.SomethingDetailsViewModel
import com.navigationtestapp.largeSample.screens.welcome.Welcome
import com.navigationtestapp.largeSample.screens.welcome.WelcomeViewModel

@OptIn(ExperimentalAnimationApi::class)
object Screens {

    val welcomeScreen = Screen(
        key = "Welcome",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            return@Screen WelcomeViewModel(router)
        },
        content = {
            val viewModel = it.get<WelcomeViewModel>()
            Welcome(viewModel::onNextClicked)
        }
    )

    val bottomMenuScreen = Screen(
        key = "BottomMenuScreen",
        onCreate = { _, _ -> BottomMenuViewModel() },
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
        key = "InnerNavigationContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            InnerNavigationViewModel(router)
        },
        content = {
            val viewModel = it.get<InnerNavigationViewModel>()
            InnerNavigationContent(viewModel::onInnerNavigationClicked, "SubScreen Menu1")
        }
    )

    val menuSecondSubScreen = Screen(
        key = "ChannelArgumentReceiveContent",
        onCreate = { channel, _ -> return@Screen ChannelArgumentReceiveViewModel(channel) },
        onDestroy = {
            val viewModel = it.get<ChannelArgumentReceiveViewModel>()
            viewModel.onDestroy()
        },
        content = {
            val secondContentViewModel = it.get<ChannelArgumentReceiveViewModel>()
            val count by secondContentViewModel.count.collectAsState()
            ChannelArgumentReceiveContent(count)
        }
    )

    val menuThirdSubScreen = Screen(
        key = "ChannelArgumentSendContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            ChannelArgumentSendViewModel(router)
        },
        onDestroy = {
            val viewModel = it.get<ChannelArgumentSendViewModel>()
            viewModel.onDestroy()
        },
        content = {
            val viewModel = it.get<ChannelArgumentSendViewModel>()
            ChannelArgumentSendContent("SubScreen Menu3", viewModel::onIncrementClicked)
        }
    )

    val someFirstDataDetailsScreen = Screen(
        key = "SomethingDetailsContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            SomethingDetailsViewModel(router)
        },
        content = {
            val viewModel = it.get<SomethingDetailsViewModel>()
            SomethingDetailsContent(viewModel)
        }
    )

    val someFirstDataDetailsScreenTwo = Screen(
        key = "ChildNavigationContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            ChildNavigationViewModel(router)
        },
        content = {
            val viewModel = it.get<ChildNavigationViewModel>()
            ChildNavigationContent(viewModel)
        }
    )

    val childInfoScreen = Screen(
        key = "RedSquareContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            RedSquareViewModel(router)
        },
        content = {
            val viewModel = it.get<RedSquareViewModel>()
            RedSquareContent(viewModel)
        }
    )

    val childInfoScreenTwo = Screen(
        key = "RedSquareContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            ChildInfoTwoViewModel(router)
        },
        content = {
            val viewModel = it.get<ChildInfoTwoViewModel>()
            RedSquareContent(viewModel)
        }
    )
}