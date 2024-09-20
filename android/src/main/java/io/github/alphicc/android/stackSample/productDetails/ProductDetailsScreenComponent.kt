package io.github.alphicc.android.stackSample.productDetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import io.github.alphicc.android.stackSample.productDetails.presentation.ProductDetailsScreen
import io.github.alphicc.android.stackSample.productDetails.presentation.ProductDetailsScreenViewModel

val productDetailsComponent = Component(
    key = "productDetailsScreenComponent",
    descriptor = Descriptor.Screen.Animated.SwipeBack,
    onCreate = { _, argument ->
        val arguments = argument.get<ProductDetailsScreenArguments>()
        return@Component ProductDetailsScreenViewModel(arguments)
    },
    onDestroy = { _ -> },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<ProductDetailsScreenViewModel>()
        val productIndex by viewModel.productIndex.collectAsState()
        ProductDetailsScreen(
            modifier = Modifier.fillMaxSize(),
            productIndex = productIndex,
            onBlackClicked = remember { { viewModel.onBackClicked() } },
            onBuyProductClicked = remember { { viewModel.onBuyProductClicked() } }
        )
    })
