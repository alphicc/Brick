package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface ContainerConnector {
    val node: StateFlow<Node?>
   // val innerNodeConnector: StateFlow<ContainerConnector?>
}