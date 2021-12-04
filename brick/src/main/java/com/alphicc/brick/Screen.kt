package com.alphicc.brick

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class Screen<D>(
    override val key: String,
    override val onCreate: ((SharedFlow<DataContainer>, DataContainer) -> D)? = null,
    override val onDestroy: ((DataContainer) -> Unit)? = null,
    val content: @Composable (DataContainer) -> Unit,
) : BaseScreen<D>(key, onCreate, onDestroy)

open class BaseScreen<D>(
    open val key: String,
    internal open val onCreate: ((SharedFlow<DataContainer>, DataContainer) -> D)?,
    internal open val onDestroy: ((DataContainer) -> Unit)?,
    internal val channel: MutableSharedFlow<DataContainer> = MutableSharedFlow(),
    internal var dependency: DataContainer? = null
)

class DataContainer(val rawData: Any?) {
    inline fun <reified C> get(): C {
        if (rawData is C) return rawData
        else throw IllegalArgumentException("Argument type incorrect")
    }
}