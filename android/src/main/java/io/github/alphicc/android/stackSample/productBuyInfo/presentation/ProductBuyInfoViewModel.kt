package io.github.alphicc.android.stackSample.productBuyInfo.presentation

import io.github.alphicc.android.stackSample.productBuyInfo.ProductBuyInfoScreenDependencies
import io.github.alphicc.android.stackSample.productBuySuccess.productBuySuccessComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductBuyInfoViewModel(arguments: ProductBuyInfoScreenDependencies) {

    private val router = arguments.router
    private val _productIndex: MutableStateFlow<Int> = MutableStateFlow(arguments.productIndex)
    val productIndex: StateFlow<Int> = _productIndex

    fun onBackClicked() {
        router.startTransaction { backComponent() }
    }

    fun onBuyProductClicked() {
        router.startTransaction { addChild(productBuySuccessComponent, router) }
    }
}