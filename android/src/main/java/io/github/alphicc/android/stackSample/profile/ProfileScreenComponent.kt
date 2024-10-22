package io.github.alphicc.android.stackSample.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import io.github.alphicc.android.stackSample.profile.presentation.ProfileScreen
import io.github.alphicc.android.stackSample.profile.presentation.ProfileScreenViewModel

val profileScreenComponent = Component(
    key = "profileScreenComponent",
    descriptor = Descriptor.Screen.Default,
    onCreate = { _, argument ->
        val dependencies = argument.get<ProfileScreenDependencies>()
        return@Component ProfileScreenViewModel(dependencies)
    },
    onDestroy = { _ -> },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<ProfileScreenViewModel>()
        val login by viewModel.login.collectAsState()
        ProfileScreen(
            modifier = Modifier.fillMaxSize(),
            onLogoutClicked = remember { { viewModel.onLogoutClicked() } },
            login = login
        )
    })
