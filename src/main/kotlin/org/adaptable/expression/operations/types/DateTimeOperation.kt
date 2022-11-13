package org.adaptable.expression.operations.types

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation
import java.time.LocalDateTime

class DateTimeOperation(
    private val localDateTime: LocalDateTime
) : Operation {
    override fun execute(context: Context): Any {
        return localDateTime
    }
}