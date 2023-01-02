package io.github.markgregg.expression.operations.types

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation
import java.time.LocalDateTime

class DateTimeOperation(
    private val localDateTime: LocalDateTime
) : Operation {
    override fun execute(context: Context): Any = localDateTime
}