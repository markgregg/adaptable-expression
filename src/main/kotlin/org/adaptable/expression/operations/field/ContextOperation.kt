package org.adaptable.expression.operations.field

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation

class ContextOperation(
    private val field: String
) : Operation {
    override fun execute(context: Context): Any {
        return context.getItem(field)
    }
}