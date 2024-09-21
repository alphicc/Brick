package io.github.alphicc.android.stackSample.productDetails.presentation

import io.github.alphicc.android.stackSample.productBuyInfo.ProductBuyInfoScreenArguments
import io.github.alphicc.android.stackSample.productBuyInfo.productBuyInfoComponent
import io.github.alphicc.android.stackSample.productDetails.ProductDetailsScreenArguments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductDetailsScreenViewModel(arguments: ProductDetailsScreenArguments) {

    private val _productIndex: MutableStateFlow<Int> = MutableStateFlow(arguments.productIndex)
    val productIndex: StateFlow<Int> = _productIndex
    private val router = arguments.router

    fun onBackClicked() {
        router.backComponent()
    }

    fun onBuyProductClicked() {
        val productBuyInfoArgument = ProductBuyInfoScreenArguments(router, _productIndex.value)
        router.addComponent(productBuyInfoComponent, productBuyInfoArgument)
    }
}