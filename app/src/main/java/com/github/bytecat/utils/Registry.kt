package com.github.bytecat.utils

import java.util.LinkedList

class Registry<T> {

    private val registers = LinkedList<T>()

    private val safeForEachList = ArrayList<T>()

    fun register(t: T) {
        if (registers.contains(t)) {
            return
        }
        registers.add(t)
    }

    fun unregister(t: T) {
        registers.remove(t)
    }

    fun unregisterAll() {
        registers.clear()
    }

    fun forEach (onEach: (T) -> Unit) {
        safeForEachList.addAll(registers)
        safeForEachList.forEach(onEach)
        safeForEachList.clear()
    }

}