package com.alphicc.brick

internal class KeyManager {

    private val keys: ArrayList<String> = ArrayList()

    fun add(key: String): Boolean {
        if (keys.contains(key)) {
            return false
        }

        keys.add(key)
        return true
    }

    fun remove(key: String) {
        keys.remove(key)
    }

    fun replaceKey(oldKey: String, newKey: String): Boolean {
        remove(oldKey)
        return add(newKey)
    }

    fun clear() = keys.clear()

    fun printKeys() {
        println("=======KEYS START========")
        keys.forEach { println(it) }
        println("=======KEYS END========")
    }
}