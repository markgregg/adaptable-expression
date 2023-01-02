package io.github.markgregg.expression.operations.types

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.Operation
import java.time.LocalDate

class DateOperation(
    private val localDate: LocalDate
) : Operation {
    override fun execute(context: Context): Any = localDate
}