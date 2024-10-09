package io.github.alphicc.android.stackSample.login.presentation

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.main.MainScreenArguments
import io.github.alphicc.android.stackSample.main.mainScreenComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginViewModel(val router: TreeRouter) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    fun onLoginClicked(login: String) {
        scope.launch {
            val ordersRouter = router.branch(mainScreenComponent.key)
            val profileRouter = router.branch(mainScreenComponent.key)
            router.startTransaction {
                val argument = MainScreenArguments(login, router, ordersRouter, profileRouter)
                newRootComponent(mainScreenComponent, argument)
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}