package io.github.alphicc.android.overlaySample.drawerScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter

val drawerScreen = Screen(
    key = "drawerScreen",
    onCreate = { _, argument ->
        val router = argument.get<TreeRouter>()
        return@Screen DrawerViewModel(router)
    },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<DrawerViewModel>()
        DrawerScreenContent(viewModel)
    }
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun DrawerScreenContent(viewModel: DrawerViewModel) {

    val isOpened by viewModel.isOpened.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) { drawerValue ->
        if (drawerValue == DrawerValue.Closed) {
            viewModel.onCloseDrawer()
        }
        return@rememberDrawerState true
    }

    LaunchedEffect(key1 = isOpened) {
        if (isOpened && drawerState.isClosed) {
            drawerState.open()
        } else if (!isOpened && drawerState.isOpen) {
            drawerState.close()
        }
    }

    LaunchedEffect(
        key1 = drawerState.currentValue,
        key2 = drawerState.isAnimationRunning
    ) {
        if (drawerState.currentValue == DrawerValue.Closed && !isOpened) {
            viewModel.onDrawerClosed()
        }
    }

    BackHandler {
        if (!drawerState.isAnimationRunning) {
            viewModel.onCloseDrawer()
        }
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(viewModel) },
        drawerContentColor = Color.Transparent,
        content = {}
    )
}

@Composable
private fun DrawerContent(viewModel: DrawerViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {

        Button(onClick = viewModel::onShowOverlayClicked) {
            Text(text = "Show Overlay")
        }
    }
}