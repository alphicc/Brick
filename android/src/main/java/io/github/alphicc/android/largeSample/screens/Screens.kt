package io.github.alphicc.android.largeSample.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
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

    val welcomeScreen = Component(
        key = "Welcome",
        descriptor = Descriptor.Main.Default,
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
        descriptor = Descriptor.Main.Default,
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
        descriptor = Descriptor.Main.Default,
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
        descriptor = Descriptor.Main.Default,
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
        descriptor = Descriptor.Main.Default,
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
        descriptor = Descriptor.Main.Default,
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
        descriptor = Descriptor.Main.Default,
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
        descriptor = Descriptor.Child,
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
        descriptor = Descriptor.Child,
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