package io.github.markgregg.expression.operations.types

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation

class LongOperation(
    private val value: Long
) : Operation {
    override fun execute(context: Context): Any = value

}