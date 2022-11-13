package org.adaptable.expression.operations.logical

import org.adaptable.expression.Context
import org.adaptable.expression.operations.BinaryOperation
import org.adaptable.expression.operations.Operation

class OrOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation) {
    override fun execute(context: Context): Any {
        return leftOperation.execute(context) as Boolean ||
                rightOperation.execute(context) as Boolean
    }
}