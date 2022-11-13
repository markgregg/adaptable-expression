package org.adaptable.expression.operations.arithematic

import org.adaptable.expression.Context
import org.adaptable.expression.exceptions.IncompatibleTypeException
import org.adaptable.expression.operations.BinaryOperation
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.Util.castToType
import java.time.LocalDate
import java.time.LocalDateTime

class MultiplyOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation) {
    override fun execute(context: Context): Any {
        return multiply(leftOperation.execute(context), rightOperation.execute(context))
    }

    private fun multiply(value1: Any, value2: Any): Any {
        return when(value1) {
            is String -> {
                throw IncompatibleTypeException("Strings cannot be multiplied")
            }
            is Long -> {
                when(value2) {
                    is Double -> value1 * value2
                    is Long -> value1 * value2
                    else -> value1 * (castToType(value2, Long::class.java) as Long)
                }
            }
            is Double -> {
                when(value2) {
                    is Double -> value1 * value2
                    is Long -> value1 * value2
                    else -> value1 * (castToType(value2, Double::class.java) as Double)
                }
            }
            is Boolean -> {
                throw IncompatibleTypeException("Booleans cannot be multiplied")
            }
            is LocalDate -> {
                throw IncompatibleTypeException("Dates cannot be multiplied")
            }
            is LocalDateTime -> {
                throw IncompatibleTypeException("Datetimes cannot be multiplied")
            }
            else -> {
                throw IncompatibleTypeException("${value1.javaClass.name} cannot be multiplied")
            }
        }
    }
}