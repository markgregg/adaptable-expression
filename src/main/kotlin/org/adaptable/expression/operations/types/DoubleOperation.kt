package org.adaptable.expression.operations.types

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation

class DoubleOperation(
    private val value: Double
) : Operation {
    override fun execute(context: Context): Any {
        return value
    }
}