package io.github.alphicc.android.stackSample.productsScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.productsScreen.presentation.ProductsScreen
import io.github.alphicc.android.stackSample.productsScreen.presentation.ProductsScreenViewModel

val productsScreenComponent = Component(
    key = "productsScreenComponent",
    descriptor = Descriptor.Screen.Default,
    onCreate = { _, argument ->
        val router = argument.get<TreeRouter>()
        return@Component ProductsScreenViewModel(router)
    },
    onDestroy = { _ -> },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<ProductsScreenViewModel>()
        ProductsScreen(
            modifier = Modifier.fillMaxSize(),
            onProductClicked = remember { { index -> viewModel.onProductClicked(index) } }
        )
    })
