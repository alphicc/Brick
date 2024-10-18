package io.github.alphicc.android.stackSample.products.presentation

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.productDetails.ProductDetailsScreenDependencies
import io.github.alphicc.android.stackSample.productDetails.productDetailsComponent

class ProductsScreenViewModel(val router: TreeRouter) {

    fun onProductClicked(index: Int) {
        val arguments = ProductDetailsScreenDependencies(
            router = router,
            productIndex = index
        )
        router.startTransaction { addComponent(productDetailsComponent, arguments) }
    }
}