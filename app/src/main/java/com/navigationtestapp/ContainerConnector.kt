package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface ContainerConnector {
    val screen: StateFlow<Screen<*>?>
    val childList: StateFlow<List<Screen<*>>>
}