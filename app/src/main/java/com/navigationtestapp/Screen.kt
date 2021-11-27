package com.navigationtestapp

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

open class Screen<D>(
    override val key: String,
    val content: @Composable (DependencyProvider) -> Unit,
    val onCreate: ((SharedFlow<ChannelArgument>, TreeRouter) -> D)? = null,
    val onDestroy: (() -> Unit)? = null
) : BaseScreen(key)

open class BaseScreen(
    internal open val key: String,
    internal val channel: MutableStateFlow<ChannelArgument> = MutableStateFlow(ChannelArgument()),
    internal var dependency: DependencyProvider? = null,
)

open class ChannelArgument {
    override fun equals(other: Any?): Boolean = false
    override fun hashCode(): Int = javaClass.hashCode()
}

class DependencyProvider(val rawData: Any?) {

    inline fun <reified C> get(): C {
        if (rawData is C) return rawData
        else throw IllegalArgumentException("Argument type incorrect")
    }
}