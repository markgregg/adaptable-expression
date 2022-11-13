package org.adaptable.expression.operations

import org.adaptable.expression.Context


interface Operation {
    fun execute(context: Context): Any
}