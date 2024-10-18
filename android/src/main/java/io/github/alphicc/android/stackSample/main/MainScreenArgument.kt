package io.github.alphicc.android.stackSample.main

sealed class MainScreenArgument {
    data class NavigateToCart(val productIndex: Int) : MainScreenArgument()
}
