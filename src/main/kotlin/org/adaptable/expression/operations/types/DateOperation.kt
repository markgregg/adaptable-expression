package org.adaptable.expression.operations.types

import org.adaptable.expression.Context
import org.adaptable.expression.operations.Operation
import java.time.LocalDate

class DateOperation(
    private val localDate: LocalDate
) : Operation {
    override fun execute(context: Context): Any {
        return localDate
    }
}