package io.github.markgregg.expression.operations.arithematic

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.BinaryOperation
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.Util.castToType
import java.time.LocalDate
import java.time.LocalDateTime

class AddOperation(
    leftOperation: Operation,
    rightOperation: Operation
) : BinaryOperation(leftOperation, rightOperation) {
    override fun execute(context: Context): Any =
        add(leftOperation.execute(context), rightOperation.execute(context))

    private fun add(value1: Any, value2: Any): Any =
        when(value1) {
            is String -> {
                value1 + (castToType(value2, String::class.java) as String)
            }
            is Long -> {
                when(value2) {
                    is Double -> value1 + value2
                    is Long -> value1 + value2
                    else -> value1 + (castToType(value2, Long::class.java) as Long)
                }
            }
            is Double -> {
                when(value2) {
                    is Double -> value1 + value2
                    is Long -> value1 + value2
                    else -> value1 + (castToType(value2, Double::class.java) as Double)
                }
            }
            is Boolean -> {
                throw IncompatibleTypeException("Booleans cannot be add")
            }
            is LocalDate -> {
                throw IncompatibleTypeException("Dates cannot be add")
            }
            is LocalDateTime -> {
                throw IncompatibleTypeException("Datetimes cannot be add")
            }
            else -> {
                throw IncompatibleTypeException("${value1.javaClass.name} cannot be add")
            }
        }

}