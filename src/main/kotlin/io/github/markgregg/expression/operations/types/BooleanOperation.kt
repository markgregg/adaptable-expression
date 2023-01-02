package io.github.markgregg.expression.operations.types

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation

class BooleanOperation(
    private val bool: Boolean
): Operation {
    override fun execute(context: Context): Any =  bool
}