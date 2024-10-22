package com.alphicc.brick

//todo refactor
internal class KeyManager {

    private val keys: ArrayList<String> = ArrayList()

    fun add(key: String): Boolean = keys.add(key)

    fun remove(key: String) = keys.remove(key)

    fun replaceKey(oldKey: String, newKey: String): Boolean {
        remove(oldKey)
        return add(newKey)
    }
}