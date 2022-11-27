package io.github.markgregg.expression.config

interface TypeLoader {
    fun override(): Boolean
    fun getExtensions(): List<Class<*>>
}