package com.alphicc.brick

import kotlinx.coroutines.flow.StateFlow

interface ContainerConnector {
    val overlay: StateFlow<Screen<*>?>
    val screen: StateFlow<Screen<*>?>
    val childList: StateFlow<List<Screen<*>>>
    val hasBackNavigationVariants: StateFlow<Boolean>

    fun back()
}