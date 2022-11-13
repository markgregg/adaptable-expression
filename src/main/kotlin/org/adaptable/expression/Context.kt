package org.adaptable.expression


interface Context {
    fun getItem(name: String): Any

}