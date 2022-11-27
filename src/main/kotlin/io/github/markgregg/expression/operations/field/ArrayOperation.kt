package io.github.markgregg.expression.operations.field

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.ArrayException
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.BinaryOperation
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.Util.getJsonValue
import io.github.markgregg.expression.operations.types.LongOperation
import io.github.markgregg.expression.operations.types.StringOperation

class ArrayOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation) {
    override fun execute(context: Context): Any {
        val target = leftOperation.execute(context)
        if( target !is ArrayNode && target !is ObjectNode) {
            throw IncompatibleTypeException("Value is not an array or map")
        }
        return getJsonValue(when (rightOperation) {
            is LongOperation -> {
                val index = rightOperation.execute(context) as Long
                (target as ArrayNode)[index.toInt()]!!
            }
            is StringOperation -> {
                val index = rightOperation.execute(context) as String
                (target as ObjectNode).get(index)!!
            }
            else -> {
                (target as ArrayNode).firstOrNull { rightOperation.execute(ArrayContext(it as ObjectNode)) as Boolean }
                    ?: throw ArrayException("Array element was not found")
            }
        })
    }

    private class ArrayContext(
        val currentElement: ObjectNode
    ) : io.github.markgregg.expression.Context {
        override fun getItem(name: String): Any {
            return getJsonValue(currentElement.get(name))
        }
    }
}