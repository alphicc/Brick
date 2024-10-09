package io.github.alphicc.android.stackSample.productBuySuccess.presentation

import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.stackSample.products.productsScreenComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductBuySuccessViewModel(private val router: TreeRouter) {

    private val viewModelScope: CoroutineScope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _dialogShowed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dialogShowed: StateFlow<Boolean> = _dialogShowed

    init {
        viewModelScope.launch {
            delay(300)
            _dialogShowed.value = true
        }
    }

    fun onDoneClicked() {
        viewModelScope.launch {
            _dialogShowed.value = false
            delay(300)
            router.startTransaction { backToComponent(productsScreenComponent.key) }
        }
    }

    fun onDestroy() {
        viewModelScope.cancel()
    }
}