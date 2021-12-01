package com.navigationtestapp.core

class KeyManager {

    private val keys: ArrayList<String> = ArrayList()

    fun add(key: String) {
        if (keys.contains(key)) {
            throw IllegalArgumentException("Key $key already existed")
        }

        keys.add(key)
    }

    fun remove(key: String) {
        keys.remove(key)
    }

    fun replaceKey(oldKey: String, newKey: String) {
        remove(oldKey)
        add(newKey)
    }

    fun clear() = keys.clear()

    fun printKeys() {
        println("=======KEYS START========")
        keys.forEach { println(it) }
        println("=======KEYS END========")
    }
}