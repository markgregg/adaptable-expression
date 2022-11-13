package org.adaptable.expression.operations.arithematic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.exceptions.CastException
import org.adaptable.expression.exceptions.IncompatibleTypeException
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.types.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime

class MinusOperationTest : FunSpec() {

    init {
        context("Minus strings") {

            listOf(
                 Triple("string",StringOperation("100"), 900L),
                 Triple("boolean",BooleanOperation(true), 999L),
                 Triple("long",LongOperation(100), 900L),
                 Triple("double",DoubleOperation(100.10), 899.9),
            ).forEach {
                test("Minus ${it.first} from long") {
                    MinusOperation(LongOperation(1000), it.second)
                        .execute(mock(Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10, 10, 10,10, 10)))
            ).forEach {
                test("Minus ${it.first} from long throws") {
                    shouldThrow<CastException> {
                        MinusOperation(LongOperation(100), it.second)
                            .execute(mock(Context::class.java))
                    }
                }
            }

            listOf(
                 Triple("string",StringOperation("10.10"), 990.0),
                 Triple("boolean",BooleanOperation(true), 999.10),
                 Triple("long",LongOperation(100), 900.10),
                 Triple("double",DoubleOperation(900.10), 100.0),
            ).forEach {
                test("Minus ${it.first} from double") {
                    MinusOperation(DoubleOperation(1000.10), it.second)
                        .execute(mock(Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Minus ${it.first} from double throws") {
                    shouldThrow<CastException> {
                        MinusOperation(DoubleOperation(100.10), it.second)
                            .execute(mock(Context::class.java))
                    }
                }
            }

            listOf(
                Pair("String",StringOperation("1000")),
                Pair("boolean",BooleanOperation(true)),
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Minus  ${it.first} from double throws") {
                    shouldThrow<IncompatibleTypeException> {
                        MinusOperation( it.second, StringOperation("test"))
                            .execute(mock(Context::class.java))
                    }
                }
            }

            test("Not handled") {
                val op = mock(Operation::class.java)
                whenever(op.execute(any())).thenReturn(10)
                shouldThrow<IncompatibleTypeException> {
                    MinusOperation( op, StringOperation("test"))
                        .execute(mock(Context::class.java))
                }
            }

        }
    }

}
