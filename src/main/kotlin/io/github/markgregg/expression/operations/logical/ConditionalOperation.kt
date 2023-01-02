package io.github.markgregg.expression.operations.logical

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.TernaryOperation

class ConditionalOperation(
    leftOperation: Operation,
    centerOperation: Operation,
    rightOperation: Operation
) : TernaryOperation(leftOperation, centerOperation, rightOperation) {
    override fun execute(context: Context): Any =
        if( leftOperation.execute(context) as Boolean) {
            centerOperation.execute(context)
        } else {
            rightOperation.execute(context)
        }

}