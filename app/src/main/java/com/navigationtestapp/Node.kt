package com.navigationtestapp

data class Node(
    var isActive: Boolean,
    var screen: Screen<*>,
    val parent: Node? = null,
    val childScreens: ArrayList<Screen<*>>,
    val neighbors: ArrayList<Node>,
    val router: TreeRouter
) {
    override fun equals(other: Any?): Boolean = false

    override fun hashCode(): Int = super.hashCode()
}