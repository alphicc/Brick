package com.navigationtestapp

data class Node(
    var isActive: Boolean,
    var screen: Screen<*>,
    val parent: Node? = null,
    val childScreens: ArrayList<Screen<*>>,
    val neighbors: ArrayList<Node>
) : BaseNode(screen.key)

open class BaseNode(key: String)
/* {
    override fun equals(other: Any?): Boolean {
        val old = other as Node?
        return (old?.screen?.key == screen.key) && old.childScreens == childScreens
    }

    override fun hashCode(): Int {
        var result = screen.hashCode()
        result = 31 * result + childScreens.hashCode()
        return result
    }
}*/