package com.yourname.calculatorapp

object HistoryManager {
    private val history = mutableListOf<String>()
    private const val MAX = 20

    fun add(entry: String) {
        history.add(0, entry)
        if (history.size > MAX) history.removeAt(history.size - 1)
    }

    fun getAll(): List<String> = history.toList()

    fun clear() { history.clear() }
}