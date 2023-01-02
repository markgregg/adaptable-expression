package io.github.markgregg.expression.operations.logical

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.BinaryOperation
import io.github.markgregg.expression.operations.Operation

class AndOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation) {
    override fun execute(context: Context): Any =
        leftOperation.execute(context) as Boolean &&
                rightOperation.execute(context) as Boolean

}