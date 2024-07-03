package com.alphicc.brick

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class AnimState(
    open val direction: AnimDirection,
    open val newKey: String,
    open val oldKeys: ImmutableList<String>
) {

    data class Executing(
        override val direction: AnimDirection,
        override val newKey: String,
        override val oldKeys: ImmutableList<String>
    ) : AnimState(direction, newKey, oldKeys)

    data object Idle : AnimState(AnimDirection.NONE, "", persistentListOf())
}

enum class AnimDirection {
    FORWARD, BACK, NONE
}