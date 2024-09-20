package io.github.alphicc.android.stackSample.productBuySuccess

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.productBuySuccess.presentation.ProductBuySuccessDialog
import io.github.alphicc.android.stackSample.productBuySuccess.presentation.ProductBuySuccessViewModel

val productBuySuccessComponent = Component(
    key = "productBuySuccessChildComponent",
    descriptor = Descriptor.Child,
    onCreate = { _, argument ->
        val router = argument.get<TreeRouter>()
        return@Component ProductBuySuccessViewModel(router)
    },
    onDestroy = { argument ->
        val viewModel = argument.get<ProductBuySuccessViewModel>()
        return@Component viewModel.onDestroy()
    },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<ProductBuySuccessViewModel>()
        val dialogShowed by viewModel.dialogShowed.collectAsState()
        ProductBuySuccessDialog(
            dialogShowed = dialogShowed,
            modifier = Modifier.fillMaxSize(),
            onDoneClicked = remember { { viewModel.onDoneClicked() } },
        )
    })
