package org.adaptable.expression.config

interface TypeLoader {
    fun override(): Boolean
    fun getExtensions(): List<Class<*>>
}