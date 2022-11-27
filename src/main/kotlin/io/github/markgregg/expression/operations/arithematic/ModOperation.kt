package io.github.markgregg.expression.operations.arithematic

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.BinaryOperation
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.Util.castToType
import java.time.LocalDate
import java.time.LocalDateTime

class ModOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation) {
    override fun execute(context: Context): Any {
        return minus(leftOperation.execute(context), rightOperation.execute(context))
    }

    private fun minus(value1: Any, value2: Any): Any {
        return when(value1) {
            is String -> {
                throw IncompatibleTypeException("Strings cannot be mod")
            }
            is Long -> {
                value1 % (castToType(value2, Long::class.java) as Long)
            }
            is Double -> {
                value1 % (castToType(value2, Double::class.java) as Double)
            }
            is Boolean -> {
                throw IncompatibleTypeException("Booleans cannot be mod")
            }
            is LocalDate -> {
                throw IncompatibleTypeException("Dates cannot be mod")
            }
            is LocalDateTime -> {
                throw IncompatibleTypeException("Datetimes cannot be mod")
            }
            else -> {
                throw IncompatibleTypeException("${value1.javaClass.name} cannot be mod")
            }
        }
    }
}