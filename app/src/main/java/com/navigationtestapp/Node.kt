package com.navigationtestapp

data class Node(
    var isActive: Boolean,
    var screen: Screen,
    val childScreens: ArrayList<Screen>,
    val neighbors: ArrayList<Node>,
    val weight: Int,
    val parent: Node? = null
) {
    override fun equals(other: Any?): Boolean = false

    override fun hashCode(): Int = super.hashCode()
}