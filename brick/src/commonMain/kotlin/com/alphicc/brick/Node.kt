package com.alphicc.brick

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.collections.immutable.*

internal data class Node(
    val rootComponent: Component<*>,
    val parent: Node? = null
) {
    private val _compositions: AtomicRef<ImmutableMap<String, Component<*>>> = atomic(persistentMapOf())
    private val _childComponents: AtomicRef<ImmutableList<Component<*>>> = atomic(persistentListOf())

    fun compositions(): ImmutableMap<String, Component<*>> = _compositions.value

    fun childComponents(): ImmutableList<Component<*>> = _childComponents.value

    fun addComposition(component: Component<*>) {
        _compositions.value = _compositions.value.toMutableMap().apply {
            this[component.key] = component
        }.toImmutableMap()
    }

    fun removeComposition(key: String) {
        _compositions.value = _compositions.value.toMutableMap().apply {
            this.remove(key)
        }.toImmutableMap()
    }

    fun addChildComponent(component: Component<*>) {
        _childComponents.value = _childComponents.value.toMutableList().apply {
            this.add(component)
        }.toImmutableList()
    }

    fun dropLastChildComponent() {
        _childComponents.value = _childComponents.value.toMutableList().apply {
            this.removeLastOrNull()
        }.toImmutableList()
    }

    fun replaceLastChildComponent(component: Component<*>) {
        _childComponents.value = _childComponents.value.toMutableList().apply {
            this[this.size - 1] = component
        }.toImmutableList()
    }
}