package com.alphicc.brick

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

internal data class Node(
    val rootComponent: Component<*>,
    val parent: Node? = null
) {
    private val _compositions: AtomicRef<Map<String, Component<*>>> = atomic(emptyMap())
    private val _childComponents: AtomicRef<List<Component<*>>> = atomic(emptyList())

    fun compositions(): Map<String, Component<*>> = _compositions.value

    fun childComponents(): List<Component<*>> = _childComponents.value

    fun addComposition(component: Component<*>) {
        _compositions.value = _compositions.value.toMutableMap().apply {
            this[component.key] = component
        }
    }

    fun removeComposition(key: String) {
        _compositions.value = _compositions.value.toMutableMap().apply {
            this.remove(key)
        }
    }

    fun addChildComponent(component: Component<*>) {
        _childComponents.value = _childComponents.value.toMutableList().apply {
            this.add(component)
        }
    }

    fun dropLastChildComponent() {
        _childComponents.value = _childComponents.value.toMutableList().apply {
            this.removeLastOrNull()
        }
    }

    fun replaceLastChildComponent(component: Component<*>) {
        _childComponents.value = _childComponents.value.toMutableList().apply {
            this[this.size - 1] = component
        }
    }
}