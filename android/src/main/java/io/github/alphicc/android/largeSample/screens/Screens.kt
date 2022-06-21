package io.github.alphicc.android.largeSample.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.screens.blueSquareChild.BlueSquareContent
import io.github.alphicc.android.largeSample.screens.blueSquareChild.BlueSquareViewModel
import io.github.alphicc.android.largeSample.screens.bottomMenu.BottomMenuScreen
import io.github.alphicc.android.largeSample.screens.bottomMenu.BottomMenuViewModel
import io.github.alphicc.android.largeSample.screens.channelArgumentReceiveSample.ChannelArgumentReceiveContent
import io.github.alphicc.android.largeSample.screens.channelArgumentReceiveSample.ChannelArgumentReceiveViewModel
import io.github.alphicc.android.largeSample.screens.channelArgumentSendSample.ChannelArgumentSendContent
import io.github.alphicc.android.largeSample.screens.channelArgumentSendSample.ChannelArgumentSendViewModel
import io.github.alphicc.android.largeSample.screens.childNavigationSample.ChildNavigationContent
import io.github.alphicc.android.largeSample.screens.childNavigationSample.ChildNavigationViewModel
import io.github.alphicc.android.largeSample.screens.innerNavigationSample.InnerNavigationContent
import io.github.alphicc.android.largeSample.screens.innerNavigationSample.InnerNavigationViewModel
import io.github.alphicc.android.largeSample.screens.redSquareChild.RedSquareContent
import io.github.alphicc.android.largeSample.screens.redSquareChild.RedSquareViewModel
import io.github.alphicc.android.largeSample.screens.somethingDetails.SomethingDetailsContent
import io.github.alphicc.android.largeSample.screens.somethingDetails.SomethingDetailsViewModel
import io.github.alphicc.android.largeSample.screens.welcome.Welcome
import io.github.alphicc.android.largeSample.screens.welcome.WelcomeViewModel

@OptIn(ExperimentalAnimationApi::class)
object Screens {

    val welcomeScreen = Screen(
        key = "Welcome",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            return@Screen WelcomeViewModel(router)
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<WelcomeViewModel>()
            Welcome(viewModel::onNextClicked)
        }
    )

    val bottomMenuScreen = Screen(
        key = "BottomMenuScreen",
        onCreate = { _, _ -> BottomMenuViewModel() },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<BottomMenuViewModel>()
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

    val innerNavigationScreen = Screen(
        key = "InnerNavigationContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            InnerNavigationViewModel(router)
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<InnerNavigationViewModel>()
            InnerNavigationContent(viewModel::onInnerNavigationClicked, "SubScreen Menu1")
        }
    )

    val channelArgumentReceiveScreen = Screen(
        key = "ChannelArgumentReceiveContent",
        onCreate = { channel, _ -> return@Screen ChannelArgumentReceiveViewModel(channel) },
        onDestroy = {
            val viewModel = it.get<ChannelArgumentReceiveViewModel>()
            viewModel.onDestroy()
        },
        content = { dataContainer, _ ->
            val secondContentViewModel = dataContainer.get<ChannelArgumentReceiveViewModel>()
            val count by secondContentViewModel.count.collectAsState()
            ChannelArgumentReceiveContent(count)
        }
    )

    val channelArgumentSendScreen = Screen(
        key = "ChannelArgumentSendContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            ChannelArgumentSendViewModel(router)
        },
        onDestroy = {
            val viewModel = it.get<ChannelArgumentSendViewModel>()
            viewModel.onDestroy()
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<ChannelArgumentSendViewModel>()
            ChannelArgumentSendContent("SubScreen Menu3", viewModel::onIncrementClicked)
        }
    )

    val somethingDetailsScreen = Screen(
        key = "SomethingDetailsContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            SomethingDetailsViewModel(router)
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<SomethingDetailsViewModel>()
            SomethingDetailsContent(viewModel)
        }
    )

    val childNavigationContentScreen = Screen(
        key = "ChildNavigationContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            ChildNavigationViewModel(router)
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<ChildNavigationViewModel>()
            ChildNavigationContent(viewModel)
        }
    )

    val redSquareScreen = Screen(
        key = "RedSquareContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            RedSquareViewModel(router)
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<RedSquareViewModel>()
            RedSquareContent(viewModel)
        }
    )

    val blueSquareScreen = Screen(
        key = "BlueSquareContent",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            BlueSquareViewModel(router)
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<BlueSquareViewModel>()
            BlueSquareContent(viewModel)
        }
    )
}