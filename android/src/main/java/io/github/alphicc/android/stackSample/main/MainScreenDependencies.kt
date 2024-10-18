package io.github.alphicc.android.stackSample.main

import com.alphicc.brick.TreeRouter

data class MainScreenDependencies(
    val login: String,
    val router: TreeRouter,
    val ordersRouter: TreeRouter,
    val profileRouter: TreeRouter
)