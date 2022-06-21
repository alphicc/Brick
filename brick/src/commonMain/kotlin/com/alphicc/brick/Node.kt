package com.alphicc.brick

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

internal data class Node(
    val screen: Screen<*>,
    val parent: Node? = null
) {
    private val _compositions: AtomicRef<Map<String, Screen<*>>> = atomic(emptyMap())
    private val _childScreens: AtomicRef<List<Screen<*>>> = atomic(emptyList())

    fun compositions(): Map<String, Screen<*>> = _compositions.value

    fun childScreens(): List<Screen<*>> = _childScreens.value

    fun addComposition(screen: Screen<*>) {
        _compositions.value = _compositions.value.toMutableMap().apply {
            this[screen.key] = screen
        }
    }

    fun removeComposition(key: String) {
        _compositions.value = _compositions.value.toMutableMap().apply {
            this.remove(key)
        }
    }

    fun addChildScreen(screen: Screen<*>) {
        _childScreens.value = _childScreens.value.toMutableList().apply {
            this.add(screen)
        }
    }

    fun dropLastChildScreen() {
        _childScreens.value = _childScreens.value.toMutableList().apply {
            this.removeLastOrNull()
        }
    }

    fun replaceLastChildScreen(screen: Screen<*>) {
        _childScreens.value = _childScreens.value.toMutableList().apply {
            this[this.size - 1] = screen
        }
    }
}