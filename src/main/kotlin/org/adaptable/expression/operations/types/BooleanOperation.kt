package org.adaptable.expression.operations.types

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation

class BooleanOperation(
    private val bool: Boolean
): Operation {
    override fun execute(context: Context): Any {
        return bool
    }
}