package org.adaptable.expression.operations.types

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation

class LongOperation(
    private val value: Long
) : Operation {
    override fun execute(context: Context): Any {
        return value
    }
}