@file:OptIn(FlowPreview::class)

package com.alphicc.brick

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

//private typealias NavAction<T> = (UpdateNavState, suspend () -> Unit, suspend () -> Unit) -> T
//private typealias UpdateNavState = suspend (AnimState.Executing) -> Unit

private data class NavAction<T>(
    val animState: AnimState.Executing,
    val preAction: suspend () -> T,
    val postAction: (suspend () -> Unit)? = null,
)

internal interface NavBus {

    val animState: StateFlow<AnimState>

    fun <T> proceed(
        animState: AnimState.Executing,
        preAction: suspend () -> T,
        postAction: (suspend () -> Unit)? = null,
    )

    fun <T> proceed(
        animState: () -> AnimState.Executing,
        preAction: suspend () -> T,
        postAction: (suspend () -> Unit)? = null,
    )

    suspend fun <T> proceedWithResult(
        animState: AnimState.Executing,
        preAction: suspend () -> T,
        postAction: (suspend () -> Unit)? = null,
    ): T

    companion object {
        fun create(scope: CoroutineScope, animDelay: Long): NavBus = NavBusImpl(scope)
    }
}

private class NavBusImpl(scope: CoroutineScope) : NavBus {

    override val animState: MutableStateFlow<AnimState> =
        MutableStateFlow(AnimState.Idle)
    private val navActionsMutex: Mutex = Mutex()
    private val navActions: MutableSharedFlow<NavAction<*>> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    init {
        println("launch in scope")
        navActions.onEach { navAction ->
            println("Mutex before")
            navActionsMutex.withLock {
                println("Mutex after")
                updateAnimState(navAction)
//                statefulNavigation {
//                    navAction.preAction()
//                    animState.value = navAction.animState
//                    delay(300)
//                    navAction.postAction?.invoke()
//                }

//                navAction(::updateAnimState)
                println("navAction after")
            }
        }.launchIn(scope)
    }

    override fun <T> proceed(
        animState: AnimState.Executing,
        preAction: suspend () -> T,
        postAction: (suspend () -> Unit)?,
    ) {
        val navAction =
            NavAction(animState = animState, preAction = preAction, postAction = postAction)
        navActions.tryEmit(navAction)
    }

    override fun <T> proceed(
        animState: () -> AnimState.Executing,
        preAction: suspend () -> T,
        postAction: (suspend () -> Unit)?
    ) {
        val navAction =
            NavAction(animState = animState(), preAction = preAction, postAction = postAction)
        navActions.tryEmit(navAction)
    }

    override suspend fun <T> proceedWithResult(
        animState: AnimState.Executing,
        preAction: suspend () -> T,
        postAction: (suspend () -> Unit)?
    ): T {
        val navAction =
            NavAction(animState = animState, preAction = preAction, postAction = postAction)
        return navActionsMutex.withLock {
            updateAnimState(navAction)
//            navAction(::updateAnimState)
        }
    }

    private suspend fun <T> updateAnimState(navAction: NavAction<T>): T = statefulNavigation {
        navAction.preAction().apply {
            delay(50)//wait until new nodes subscribe on flow data
            animState.value = navAction.animState
            delay(300)
            /**
             *             val duration = if (navAction.animState.direction == AnimDirection.NONE) 0L else 300L
             *             delay(duration)
             */
            navAction.postAction?.invoke()
        }
    }

    private suspend inline fun <T> statefulNavigation(
        crossinline navigateAction: suspend () -> T
    ): T {
        return try {
            println("invoke navigate action")
            navigateAction.invoke()
        } catch (exception: Exception) {
            println("exception handle \n${exception.stackTraceToString()}")
            throw exception
        } finally {
            println("idle call")
            animState.value = AnimState.Idle
        }
    }
}