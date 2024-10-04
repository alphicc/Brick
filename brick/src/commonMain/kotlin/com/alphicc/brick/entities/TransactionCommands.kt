package com.alphicc.brick.entities

sealed class TransactionCommands {
    data class MicroTransaction(
        val id: String,
        val navAction: NavAction<*>,
    ) : TransactionCommands()

    data class OpenTransaction(val scope: TransactionScope) : TransactionCommands()

    data class FinishTransaction(val id: String) : TransactionCommands()
}
