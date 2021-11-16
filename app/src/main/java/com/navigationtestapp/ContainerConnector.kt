package com.navigationtestapp

import kotlinx.coroutines.flow.StateFlow

interface ContainerConnector {
    val currentNode: StateFlow<Node?>
    val innerNodeConnector: StateFlow<ContainerConnector?>
}