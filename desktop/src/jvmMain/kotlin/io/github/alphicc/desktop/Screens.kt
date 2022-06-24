package io.github.alphicc.desktop

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.Component
import com.alphicc.brick.TreeRouter
import io.github.alphicc.desktop.blueSquareChild.BlueSquareContent
import io.github.alphicc.desktop.blueSquareChild.BlueSquareViewModel
import io.github.alphicc.desktop.bottomMenu.BottomMenuScreen
import io.github.alphicc.desktop.bottomMenu.BottomMenuViewModel
import io.github.alphicc.desktop.channelArgumentReceiveSample.ChannelArgumentReceiveContent
import io.github.alphicc.desktop.channelArgumentReceiveSample.ChannelArgumentReceiveViewModel
import io.github.alphicc.desktop.channelArgumentSendSample.ChannelArgumentSendContent
import io.github.alphicc.desktop.channelArgumentSendSample.ChannelArgumentSendViewModel
import io.github.alphicc.desktop.childNavigationSample.ChildNavigationContent
import io.github.alphicc.desktop.childNavigationSample.ChildNavigationViewModel
import io.github.alphicc.desktop.innerNavigationSample.InnerNavigationContent
import io.github.alphicc.desktop.innerNavigationSample.InnerNavigationViewModel
import io.github.alphicc.desktop.redSquareChild.RedSquareContent
import io.github.alphicc.desktop.redSquareChild.RedSquareViewModel
import io.github.alphicc.desktop.somethingDetails.SomethingDetailsContent
import io.github.alphicc.desktop.somethingDetails.SomethingDetailsViewModel
import io.github.alphicc.desktop.welcome.Welcome
import io.github.alphicc.desktop.welcome.WelcomeViewModel

@OptIn(ExperimentalAnimationApi::class)
object Screens {

    val welcomeScreen = Component(
        key = "Welcome",
        onCreate = { _, argument ->
            val router = argument.get<TreeRouter>()
            return@Component WelcomeViewModel(router)
        },
        content = { dataContainer, _ ->
            val viewModel = dataContainer.get<WelcomeViewModel>()
            Welcome(viewModel::onNextClicked)
        }
    )

    val bottomMenuScreen = Component(
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

    val innerNavigationScreen = Component(
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

    val channelArgumentReceiveScreen = Component(
        key = "ChannelArgumentReceiveContent",
        onCreate = { channel, _ -> return@Component ChannelArgumentReceiveViewModel(channel) },
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

    val channelArgumentSendScreen = Component(
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

    val somethingDetailsScreen = Component(
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

    val childNavigationContentScreen = Component(
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

    val redSquareScreen = Component(
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

    val blueSquareScreen = Component(
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