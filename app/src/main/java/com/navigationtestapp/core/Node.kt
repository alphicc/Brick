package com.navigationtestapp.core

data class Node(
    var screen: Screen<*>,
    val parent: Node? = null,
    val childScreens: ArrayList<Screen<*>>,
    val neighbors: ArrayList<Node>
)