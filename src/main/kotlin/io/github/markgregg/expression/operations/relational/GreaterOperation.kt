package io.github.markgregg.expression.operations.relational

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.BinaryOperation
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.Util.castToType
import java.time.LocalDate
import java.time.LocalDateTime

class GreaterOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation)  {
    override fun execute(context: Context): Any =
        isGreater(leftOperation.execute(context), rightOperation.execute(context))

    private fun isGreater(value1: Any, value2: Any): Boolean =
        when(value1) {
            is String -> {
                value1 > (castToType(value2, String::class.java) as String)
            }
            is Long -> {
                value1 > (castToType(value2, Long::class.java) as Long)
            }
            is Double -> {
                value1 > (castToType(value2, Double::class.java) as Double)
            }
            is LocalDate -> {
                value1 > (castToType(value2, LocalDate::class.java) as LocalDate)
            }
            is LocalDateTime -> {
                value1 > (castToType(value2, LocalDateTime::class.java) as LocalDateTime)
            }
            else -> {
                throw IncompatibleTypeException("${value1.javaClass.name} cannot be compared")
            }
        }

}