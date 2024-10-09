package io.github.alphicc.android.stackSample.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import io.github.alphicc.android.stackSample.main.presentation.MainScreen
import io.github.alphicc.android.stackSample.main.presentation.MainViewModel

val mainScreenComponent = Component(
    key = "mainScreenComponent",
    descriptor = Descriptor.Screen.Animated.CrossFade(),
    onCreate = { _, argument ->
        val arguments = argument.get<MainScreenArguments>()
        return@Component MainViewModel(arguments)
    },
    onDestroy = { argument ->
        val viewModel = argument.get<MainViewModel>()
        viewModel.onCleared()
    },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<MainViewModel>()
        val login by viewModel.login.collectAsState()
        val indexMenu by viewModel.selectedMenuIndex.collectAsState()
        val childRouter by viewModel.currentRouter.collectAsState()
        MainScreen(
            modifier = Modifier.fillMaxSize(),
            login = login,
            indexMenu = indexMenu,
            containerConnector = childRouter,
        )
    })