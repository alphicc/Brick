package com.alphicc.brick.entities

import com.alphicc.brick.AnimState
import com.alphicc.brick.TransactionSettings

data class NavAction<T>(
    val settings: TransactionSettings,
    val animState: () -> AnimState.Executing,
    val preAction: suspend () -> T,
    val postAction: (suspend () -> Unit)? = null,
    val filter: (() -> Boolean)? = null,
)
