package io.github.alphicc.android.stackSample.productBuyInfo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import io.github.alphicc.android.stackSample.productBuyInfo.presentation.ProductBuyInfoScreen
import io.github.alphicc.android.stackSample.productBuyInfo.presentation.ProductBuyInfoViewModel
import io.github.alphicc.android.stackSample.productDetails.ProductDetailsScreenArguments
import io.github.alphicc.android.stackSample.productDetails.presentation.ProductDetailsScreen
import io.github.alphicc.android.stackSample.productDetails.presentation.ProductDetailsScreenViewModel


val productBuyInfoComponent = Component(
    key = "productBuyInfoScreenComponent",
    descriptor = Descriptor.Screen.Animated.SwipeBack,
    onCreate = { _, argument ->
        val arguments = argument.get<ProductBuyInfoScreenArguments>()
        return@Component ProductBuyInfoViewModel(arguments)
    },
    onDestroy = { _ -> },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<ProductBuyInfoViewModel>()
        val productIndex by viewModel.productIndex.collectAsState()
        ProductBuyInfoScreen(
            modifier = Modifier.fillMaxSize(),
            productIndex = productIndex,
            onBackClicked = remember { { viewModel.onBackClicked() } },
            onBuyClicked = remember { { viewModel.onBuyProductClicked() } }
        )
    })