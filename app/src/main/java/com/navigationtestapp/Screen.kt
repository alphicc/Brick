package com.navigationtestapp

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

data class Screen<D>(
    override val key: String,
    override val onCreate: ((SharedFlow<ChannelArgument>, DataProvider) -> D)? = null,
    override val onDestroy: (() -> Unit)? = null,
    val content: @Composable (DataProvider) -> Unit,
) : BaseScreen<D>(key, onCreate, onDestroy)

open class BaseScreen<D>(
    internal open val key: String,
    internal open val onCreate: ((SharedFlow<ChannelArgument>, DataProvider) -> D)?,
    internal open val onDestroy: (() -> Unit)?,
    internal val channel: MutableSharedFlow<ChannelArgument> = MutableStateFlow(ChannelArgument()),
    internal var dependency: DataProvider? = null
)

open class ChannelArgument {
    override fun equals(other: Any?): Boolean = false
    override fun hashCode(): Int = javaClass.hashCode()
}

class DataProvider(val rawData: Any?) {
    inline fun <reified C> get(): C {
        if (rawData is C) return rawData
        else throw IllegalArgumentException("Argument type incorrect")
    }
}