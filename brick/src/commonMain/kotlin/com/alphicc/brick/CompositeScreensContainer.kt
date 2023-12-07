package com.alphicc.brick

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableMap

class CompositeContainer(private val compositions: ImmutableMap<String, Component<*>>) {

    @Composable
    fun place(key: String) {
        val composition = compositions[key]
        composition?.run {
            showContent(
                dataContainer = this.dependency ?: throw IllegalArgumentException("Dependency can not be null"),
                compositions = compositions
            )
        }
    }
}