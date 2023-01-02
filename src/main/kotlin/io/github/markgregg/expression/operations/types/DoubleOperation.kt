package io.github.markgregg.expression.operations.types

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation

class DoubleOperation(
    private val value: Double
) : Operation {
    override fun execute(context: Context): Any = value
}