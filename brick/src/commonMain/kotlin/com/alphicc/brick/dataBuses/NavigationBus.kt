package com.alphicc.brick.dataBuses

import com.alphicc.brick.AnimState
import com.alphicc.brick.entities.NavAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val BUFFER_CAPACITY = 10

internal interface NavigationBus {

    val animState: StateFlow<AnimState>

    suspend fun proceed(navAction: NavAction<*>)

    suspend fun <T> proceedWithResult(navAction: NavAction<T>): T

    companion object {
        fun create(scope: CoroutineScope): NavigationBus = NavigationBusImpl(scope)
    }
}

private class NavigationBusImpl(scope: CoroutineScope) : NavigationBus {

    override val animState: MutableStateFlow<AnimState> = MutableStateFlow(AnimState.Idle)

    private val actionsMutex: Mutex = Mutex()
    private val actionsFlow: MutableSharedFlow<NavAction<*>> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    init {
        actionsFlow.onEach { navAction ->
            actionsMutex.withLock {
                if (navAction.filter?.invoke() != false) {
                    updateAnimState(navAction)
                }
            }
        }.launchIn(scope)
    }

    override suspend fun proceed(navAction: NavAction<*>) = actionsFlow.emit(navAction)

    override suspend fun <T> proceedWithResult(navAction: NavAction<T>): T {
        return actionsMutex.withLock {
            updateAnimState(navAction)
        }
    }

    private suspend fun <T> updateAnimState(navAction: NavAction<T>): T = statefulNavigation {
        val newAnimState = navAction.animState()
        navAction.preAction().apply {
            //wait until new nodes subscribe on flow data
            val settings = navAction.settings
            delay(settings.subscribeDelay)
            animState.value = newAnimState
            delay(settings.animDurationDelay)
            navAction.postAction?.invoke()
        }
    }

    private suspend inline fun <T> statefulNavigation(crossinline navigateAction: suspend () -> T): T {
        return try {
            navigateAction.invoke()
        } catch (exception: Exception) {
            throw exception
        } finally {
            animState.value = AnimState.Idle
        }
    }
}