package com.alphicc.brick

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class Screen<D>(
    override val key: String,
    override val onCreate: ((SharedFlow<DataContainer>, DataContainer) -> D)? = null,
    override val onDestroy: ((DataContainer) -> Unit)? = null,
    private val content: @Composable (DataContainer, CompositeContainer) -> Unit
) : BaseScreen<D>(key, onCreate, onDestroy) {

    @Composable
    internal fun showContent(dataContainer: DataContainer, compositions: Map<String, Screen<*>>) {
        val compositeContainer = CompositeContainer(compositions)
        content.invoke(dataContainer, compositeContainer)
    }
}

open class BaseScreen<D>(
    open val key: String,
    internal open val onCreate: ((SharedFlow<DataContainer>, DataContainer) -> D)?,
    internal open val onDestroy: ((DataContainer) -> Unit)?,
    internal val channel: MutableSharedFlow<DataContainer> = MutableSharedFlow(),
    internal var dependency: DataContainer? = null
) {
    internal open fun <A> onCreate(initialArgument: A): BaseScreen<*> {
        val argumentDataContainer = DataContainer(initialArgument)
        val dependency = onCreate?.invoke(channel, argumentDataContainer)
        val dependencyDataContainer = DataContainer(dependency)
        this.dependency = dependencyDataContainer
        return this
    }

    internal open fun onDestroy() {
        onDestroy?.invoke(dependency ?: DataContainer(null))
    }
}

class DataContainer(val rawData: Any?) {
    inline fun <reified C> get(): C {
        if (rawData is C) return rawData
        else throw IllegalArgumentException("Argument type incorrect")
    }
}