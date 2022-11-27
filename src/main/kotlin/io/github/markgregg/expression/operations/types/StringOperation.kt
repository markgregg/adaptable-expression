package io.github.markgregg.expression.operations.types

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation

class StringOperation(
    private val text: String,
) : Operation {
    override fun execute(context: Context): Any {
        return text
    }
}