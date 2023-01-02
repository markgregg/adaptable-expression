package io.github.markgregg.expression.operations.field

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation

class ContextOperation(
    private val field: String
) : Operation {
    override fun execute(context: Context): Any =
        context.getItem(field)

}