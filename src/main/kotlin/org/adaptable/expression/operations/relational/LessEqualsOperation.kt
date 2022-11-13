package org.adaptable.expression.operations.relational

import org.adaptable.expression.Context
import org.adaptable.expression.exceptions.IncompatibleTypeException
import org.adaptable.expression.operations.BinaryOperation
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.Util.castToType
import java.time.LocalDate
import java.time.LocalDateTime

class LessEqualsOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation)  {
    override fun execute(context: Context): Any {
        return isLessEquals(leftOperation.execute(context), rightOperation.execute(context))
    }

    private fun isLessEquals(value1: Any, value2: Any): Boolean {
        return when(value1) {
            is String -> {
                value1 <= (castToType(value2, String::class.java) as String)
            }
            is Long -> {
                value1 <= (castToType(value2, Long::class.java) as Long)
            }
            is Double -> {
                value1 <= (castToType(value2, Double::class.java) as Double)
            }
            is LocalDate -> {
                value1 <= (castToType(value2, LocalDate::class.java) as LocalDate)
            }
            is LocalDateTime -> {
                value1 <= (castToType(value2, LocalDateTime::class.java) as LocalDateTime)
            }
            else -> {
                throw IncompatibleTypeException("${value1.javaClass.name} cannot be compared")
            }
        }
    }
}