package io.github.alphicc.android.stackSample.productDetails.presentation

import io.github.alphicc.android.stackSample.main.MainScreenArgument
import io.github.alphicc.android.stackSample.main.mainScreenComponent
import io.github.alphicc.android.stackSample.productDetails.ProductDetailsArguments
import io.github.alphicc.android.stackSample.productDetails.ProductDetailsScreenDependencies
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

class ProductDetailsScreenViewModel(
    arguments: ProductDetailsScreenDependencies,
    private val argumentsFlow: Flow<ProductDetailsArguments>
) {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _productIndex: MutableStateFlow<Int> = MutableStateFlow(arguments.productIndex)
    val productIndex: StateFlow<Int> = _productIndex
    private val router = arguments.router

    init {
        observeArguments()
    }

    fun onBackClicked() {
        router.startTransaction { backComponent() }
    }

    fun onBuyProductClicked() {
        scope.launch {
            val mainScreenArgument = MainScreenArgument.NavigateToCart(_productIndex.value)
            router.passArgument(mainScreenComponent.key, mainScreenArgument)
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun observeArguments() {
        argumentsFlow.onEach { argument ->
            when (argument) {
                ProductDetailsArguments.CloseScreen -> router.onBackClicked()
            }
        }.launchIn(scope)
    }
}