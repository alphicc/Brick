package io.github.alphicc.android.stackSample.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.login.presentation.LoginScreen
import io.github.alphicc.android.stackSample.login.presentation.LoginViewModel

val loginScreenComponent = Component(
    key = "loginScreenComponent",
    descriptor = Descriptor.Screen.Default,
    onCreate = { _, argument ->
        val router = argument.get<TreeRouter>()
        return@Component LoginViewModel(router)
    },
    onDestroy = { argument ->
        val viewModel = argument.get<LoginViewModel>()
        viewModel.onCleared()
    },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<LoginViewModel>()
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            onLoginClicked = viewModel::onLoginClicked,
        )
    })