package io.github.alphicc.android.stackSample.productsScreen.presentation

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.productDetails.ProductDetailsScreenArguments
import io.github.alphicc.android.stackSample.productDetails.productDetailsComponent
import io.github.alphicc.android.stackSample.productsScreen.productsScreenComponent

class ProductsScreenViewModel(val router: TreeRouter) {

    fun onProductClicked(index: Int) {
        val arguments = ProductDetailsScreenArguments(
            router = router,
            productIndex = index
        )
        router.startTransaction { addComponent(productDetailsComponent, arguments) }
    }
}