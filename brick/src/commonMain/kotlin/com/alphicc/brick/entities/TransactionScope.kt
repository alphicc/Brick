package com.alphicc.brick.entities

import com.alphicc.brick.TransactionSettings
import com.alphicc.brick.TreeRouter

data class TransactionScope internal constructor(
    val scopeId: String,
    val settings: TransactionSettings,
    private val treeRouter: TreeRouter
): TreeRouter by treeRouter