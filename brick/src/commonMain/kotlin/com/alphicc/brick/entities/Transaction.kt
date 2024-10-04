package com.alphicc.brick.entities

data class Transaction(
    val scope: TransactionScope,
    val navActions: List<NavAction<*>>
)
