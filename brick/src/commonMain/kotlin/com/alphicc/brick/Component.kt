package com.alphicc.brick

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class Component<D>(
    override val key: String,
    override val onCreate: ((SharedFlow<DataContainer>, DataContainer) -> D)? = null,
    override val onDestroy: ((DataContainer) -> Unit)? = null,
    override val keepAliveCompose: Boolean = false,
    private val content: @Composable (DataContainer, CompositeContainer) -> Unit
) : BaseComponent<D>(key, onCreate, onDestroy, keepAliveCompose) {

    @Composable
    internal fun showContent(dataContainer: DataContainer, compositions: Map<String, Component<*>>) {
        val compositeContainer = CompositeContainer(compositions)
        content.invoke(dataContainer, compositeContainer)
    }
}

open class BaseComponent<D>(
    open val key: String,
    internal open val onCreate: ((SharedFlow<DataContainer>, DataContainer) -> D)?,
    internal open val onDestroy: ((DataContainer) -> Unit)?,
    internal open val keepAliveCompose: Boolean,
    internal val channel: MutableSharedFlow<DataContainer> = MutableSharedFlow(),
    internal var dependency: DataContainer? = null
) {
    internal open fun <A> onCreate(initialArgument: A): BaseComponent<*> {
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