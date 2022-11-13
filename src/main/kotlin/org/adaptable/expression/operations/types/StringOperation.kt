package org.adaptable.expression.operations.types

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation

class StringOperation(
    private val text: String,
) : Operation {
    override fun execute(context: Context): Any {
        return text
    }
}