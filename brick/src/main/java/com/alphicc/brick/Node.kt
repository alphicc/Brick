package com.alphicc.brick

internal data class Node(
    var screen: Screen<*>,
    val parent: Node? = null,
    val childScreens: ArrayList<Screen<*>>
)