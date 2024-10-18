package io.github.alphicc.android.stackSample.profile.presentation

import io.github.alphicc.android.stackSample.profile.ProfileScreenDependencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileScreenViewModel(dependencies: ProfileScreenDependencies) {

    private val _login: MutableStateFlow<String> = MutableStateFlow(dependencies.login)
    val login: StateFlow<String> = _login
}