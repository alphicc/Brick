package com.alphicc.brick

import androidx.compose.runtime.Composable

class CompositeContainer(private val compositions: Map<String, Component<*>>) {

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