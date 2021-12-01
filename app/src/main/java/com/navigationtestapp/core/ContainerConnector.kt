package com.navigationtestapp.core

import kotlinx.coroutines.flow.StateFlow

interface ContainerConnector {
    val screen: StateFlow<Screen<*>?>
    val childList: StateFlow<List<Screen<*>>>
    val hasBackNavigationVariants: StateFlow<Boolean>

    fun back()
}