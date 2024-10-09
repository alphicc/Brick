package io.github.alphicc.android.stackSample.main.presentation

import com.alphicc.brick.TransactionSettings
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.main.MainScreenArguments
import io.github.alphicc.android.stackSample.main.mainScreenComponent
import io.github.alphicc.android.stackSample.products.productsScreenComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(mainScreenArguments: MainScreenArguments) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val rootRouter: TreeRouter = mainScreenArguments.router
    private val ordersRouter: TreeRouter = mainScreenArguments.ordersRouter
    private val profileRouter: TreeRouter = mainScreenArguments.profileRouter

    val login: MutableStateFlow<String> = MutableStateFlow(mainScreenArguments.login)

    private val _currentRouter: MutableStateFlow<TreeRouter?> = MutableStateFlow(null)
    val currentRouter: StateFlow<TreeRouter?> = _currentRouter

    private val _selectedMenuIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedMenuIndex: StateFlow<Int> = _selectedMenuIndex

    init {
        scope.launch {
            initOrdersRouter()
            initProfileRouter()
            _currentRouter.value = ordersRouter
        }
    }

    fun onOrderClicked() {
        _currentRouter.value = ordersRouter
        _selectedMenuIndex.value = 0
    }

    fun onProfileClicked() {
        _currentRouter.value = profileRouter
        _selectedMenuIndex.value = 2
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun initOrdersRouter() {
        ordersRouter.startTransaction(transactionSettings = TransactionSettings.immediately()) {
            addComponent(productsScreenComponent, ordersRouter)
        }
    }

    private fun initProfileRouter() {
        profileRouter.startTransaction(transactionSettings = TransactionSettings.immediately()) {
            addComponent(productsScreenComponent, profileRouter)
        }
    }
}