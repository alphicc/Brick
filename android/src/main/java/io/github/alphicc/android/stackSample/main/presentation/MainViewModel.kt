package io.github.alphicc.android.stackSample.main.presentation

import com.alphicc.brick.TransactionSettings
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.main.MainScreenArgument
import io.github.alphicc.android.stackSample.main.MainScreenDependencies
import io.github.alphicc.android.stackSample.productBuyInfo.ProductBuyInfoScreenDependencies
import io.github.alphicc.android.stackSample.productBuyInfo.productBuyInfoComponent
import io.github.alphicc.android.stackSample.products.productsScreenComponent
import io.github.alphicc.android.stackSample.profile.ProfileScreenDependencies
import io.github.alphicc.android.stackSample.profile.profileScreenComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    mainScreenDependencies: MainScreenDependencies,
    private val arguments: Flow<MainScreenArgument>
) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val rootRouter: TreeRouter = mainScreenDependencies.router
    private val ordersRouter: TreeRouter = mainScreenDependencies.ordersRouter
    private val profileRouter: TreeRouter = mainScreenDependencies.profileRouter

    val login: MutableStateFlow<String> = MutableStateFlow(mainScreenDependencies.login)

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
        observeArguments()
    }

    fun onMenuClicked(index: Int) {
        when (index) {
            0 -> onOrderClicked()
            else -> onProfileClicked()
        }
    }

    fun onBackClicked() {
        val currentScreen = _currentRouter.value?.lastComponentKey()
        if (currentScreen == null || currentScreen == productsScreenComponent.key) {
            rootRouter.onBackClicked()
        } else _currentRouter.value?.onBackClicked()
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun onOrderClicked() {
        _currentRouter.value = ordersRouter
        _selectedMenuIndex.value = 0
    }

    private fun onProfileClicked() {
        _currentRouter.value = profileRouter
        _selectedMenuIndex.value = 2
    }

    private fun initOrdersRouter() {
        ordersRouter.startTransaction(transactionSettings = TransactionSettings.immediately()) {
            addComponent(productsScreenComponent, ordersRouter)
        }
    }

    private fun initProfileRouter() {
        profileRouter.startTransaction(transactionSettings = TransactionSettings.immediately()) {
            val profileScreenDependencies = ProfileScreenDependencies(profileRouter, login.value)
            addComponent(profileScreenComponent, profileScreenDependencies)
        }
    }

    private fun observeArguments() {
        arguments.onEach { argument ->
            when (argument) {
                is MainScreenArgument.NavigateToCart -> {
                    val productBuyInfoArgument =
                        ProductBuyInfoScreenDependencies(rootRouter, argument.productIndex)
                    rootRouter.startTransaction {
                        addComponent(
                            productBuyInfoComponent,
                            productBuyInfoArgument
                        )
                    }
                }
            }
        }.launchIn(scope)
    }
}