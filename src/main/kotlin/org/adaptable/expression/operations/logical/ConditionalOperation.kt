package org.adaptable.expression.operations.logical

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.TernaryOperation

class ConditionalOperation(
    leftOperation: Operation,
    centerOperation: Operation,
    rightOperation: Operation
) : TernaryOperation(leftOperation, centerOperation, rightOperation) {
    override fun execute(context: Context): Any {
        return if( leftOperation.execute(context) as Boolean) {
            centerOperation.execute(context)
        } else {
            rightOperation.execute(context)
        }
    }
}