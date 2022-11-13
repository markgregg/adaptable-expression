package org.adaptable.expression.operations.logical

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.UnaryOperation
import org.adaptable.expression.operations.Util.castToType

class NotOperation(
    operation: Operation,
) : UnaryOperation(operation) {
    override fun execute(context: Context): Any {
        return !(castToType(operation.execute(context), Boolean::class.java) as Boolean)
    }
}