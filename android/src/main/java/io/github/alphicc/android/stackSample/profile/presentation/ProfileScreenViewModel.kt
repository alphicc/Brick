package io.github.alphicc.android.stackSample.profile.presentation

import com.alphicc.brick.TransactionSettings
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.login.loginScreenComponent
import io.github.alphicc.android.stackSample.profile.ProfileScreenDependencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileScreenViewModel(dependencies: ProfileScreenDependencies) {

    private val _login: MutableStateFlow<String> = MutableStateFlow(dependencies.login)
    val login: StateFlow<String> = _login
    private val treeRouter: TreeRouter = dependencies.profileRouter

    fun onLogoutClicked() {
        val rootRouter = treeRouter.getRootRouter()
        rootRouter.startTransaction(transactionSettings = TransactionSettings.immediately()) {
            newRootComponent(loginScreenComponent, rootRouter)
        }
    }
}