package io.github.markgregg.expression


interface Context {
    fun getItem(name: String): Any

}