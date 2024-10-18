package com.alphicc.brick

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@InternalApi
sealed class AnimState(
    open val direction: AnimDirection,
    open val newKey: String,
    open val oldKeys: ImmutableList<String>
) {

    data class Executing(
        override val direction: AnimDirection,
        override val newKey: String,
        override val oldKeys: ImmutableList<String> = persistentListOf(),
    ) : AnimState(direction, newKey, oldKeys)

    data object Idle : AnimState(AnimDirection.ON_PLACE, "", persistentListOf())
}

@InternalApi
enum class AnimDirection {
    FORWARD, BACK, ON_PLACE
}