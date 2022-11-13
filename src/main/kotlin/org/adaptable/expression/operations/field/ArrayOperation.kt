package org.adaptable.expression.operations.field

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.adaptable.expression.Context
import org.adaptable.expression.exceptions.ArrayException
import org.adaptable.expression.exceptions.IncompatibleTypeException
import org.adaptable.expression.operations.BinaryOperation
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.Util.getJsonValue
import org.adaptable.expression.operations.types.LongOperation
import org.adaptable.expression.operations.types.StringOperation

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
    ) : Context {
        override fun getItem(name: String): Any {
            return getJsonValue(currentElement.get(name))
        }
    }
}