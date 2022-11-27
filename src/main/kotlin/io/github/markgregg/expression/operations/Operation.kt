package io.github.markgregg.expression.operations

import io.github.markgregg.expression.Context


interface Operation {
    fun execute(context: Context): Any
}