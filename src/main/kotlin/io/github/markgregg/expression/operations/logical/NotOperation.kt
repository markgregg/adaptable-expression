package io.github.markgregg.expression.operations.logical

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.UnaryOperation
import io.github.markgregg.expression.operations.Util.castToType

class NotOperation(
    operation: Operation,
) : UnaryOperation(operation) {
    override fun execute(context: Context): Any =
        !(castToType(operation.execute(context), Boolean::class.java) as Boolean)

}