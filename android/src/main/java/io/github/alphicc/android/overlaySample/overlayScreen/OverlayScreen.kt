package io.github.alphicc.android.overlaySample.overlayScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter

val overlayScreen = Component(
    key = "overlayScreen",
    descriptor = Descriptor.Overlay,
    onCreate = { _, argument ->
        val treeRouter = argument.get<TreeRouter>()
        return@Component OverlayViewModel(treeRouter)
    },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<OverlayViewModel>()
        OverlayScreenContent(viewModel)
    }
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun OverlayScreenContent(viewModel: OverlayViewModel) {

    val isVisible by viewModel.isVisible.collectAsState()
    val mutableTransitionState = remember { MutableTransitionState(false) }

    LaunchedEffect(key1 = isVisible) {
        mutableTransitionState.targetState = isVisible
    }

    LaunchedEffect(
        key1 = mutableTransitionState.currentState,
        key2 = mutableTransitionState.isIdle
    ) {
        if (!mutableTransitionState.currentState && !isVisible) {
            viewModel.onOverlayHidden()
        }
    }

    AnimatedVisibility(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        visibleState = mutableTransitionState,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
    ) {

        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(128.dp)
                .fillMaxWidth(),
            elevation = 16.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(modifier = Modifier.align(Alignment.Center), text = "Overlay!")

                Button(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onClick = viewModel::onRemoveOverlayClicked
                ) {
                    Text(text = "Remove")
                }
            }
        }
    }
}