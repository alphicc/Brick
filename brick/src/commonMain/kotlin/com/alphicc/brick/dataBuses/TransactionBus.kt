package com.alphicc.brick.dataBuses

import com.alphicc.brick.TransactionSettings
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.entities.NavAction
import com.alphicc.brick.entities.Transaction
import com.alphicc.brick.entities.TransactionCommands
import com.alphicc.brick.entities.TransactionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlin.random.Random

internal interface TransactionBus {

    fun startTransaction(
        router: TreeRouter,
        transactionSettings: TransactionSettings,
        action: TransactionScope.() -> Unit
    )

    fun publishMicroTransaction(id: String, navAction: NavAction<*>)

    companion object {
        fun create(
            scope: CoroutineScope,
            navigationBus: NavigationBus
        ): TransactionBus = TransactionBusImpl(scope, navigationBus)
    }
}

private class TransactionBusImpl(
    scope: CoroutineScope,
    private val navigationBus: NavigationBus
) : TransactionBus {

    private val transactionsMap: MutableMap<String, Transaction> = mutableMapOf()
    private val commandsFlow: MutableSharedFlow<TransactionCommands> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    init {
        commandsFlow.onEach { command ->
            when (command) {
                is TransactionCommands.OpenTransaction -> handleOpenTransactionCommand(command)
                is TransactionCommands.FinishTransaction -> handleFinishTransaction(command)
                is TransactionCommands.MicroTransaction -> handleMicroTransactionCommand(command)
            }
        }.launchIn(scope)
    }

    override fun startTransaction(
        router: TreeRouter,
        transactionSettings: TransactionSettings,
        action: TransactionScope.() -> Unit
    ) {
        val id = "${Clock.System.now().toEpochMilliseconds()}_${Random.nextLong()}"
        val transactionScope = TransactionScope(id, transactionSettings, router)
        val openTransaction = TransactionCommands.OpenTransaction(transactionScope)
        commandsFlow.tryEmit(openTransaction)
        action(transactionScope)
        val finishTransaction = TransactionCommands.FinishTransaction(id)
        commandsFlow.tryEmit(finishTransaction)
    }

    override fun publishMicroTransaction(
        id: String,
        navAction: NavAction<*>
    ) {
        val command = TransactionCommands.MicroTransaction(id, navAction)
        commandsFlow.tryEmit(command)
    }

    private suspend fun handleFinishTransaction(command: TransactionCommands.FinishTransaction) {
        val transaction = transactionsMap.remove(command.id)
        transaction?.navActions?.forEach { navAction ->
            navigationBus.proceed(navAction)
        }
    }

    private fun handleOpenTransactionCommand(command: TransactionCommands.OpenTransaction) {
        transactionsMap[command.scope.id] = Transaction(
            scope = command.scope,
            navActions = emptyList()
        )
    }

    private fun handleMicroTransactionCommand(command: TransactionCommands.MicroTransaction) {
        val transaction = transactionsMap[command.id]
        if (transaction != null) {
            val updatedTransaction = transaction.copy(
                navActions = transaction.navActions.toMutableList().apply {
                    add(command.navAction)
                }
            )
            transactionsMap[command.id] = updatedTransaction
        }
    }
}