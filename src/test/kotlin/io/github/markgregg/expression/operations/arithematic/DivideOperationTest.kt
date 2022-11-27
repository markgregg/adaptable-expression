package io.github.markgregg.expression.operations.arithematic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.CastException
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.types.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime

class DivideOperationTest : FunSpec() {

    init {
        context("Divide") {

            listOf(
                 Triple("string",StringOperation("100"), 10L),
                 Triple("boolean",BooleanOperation(true), 1000L),
                 Triple("long",LongOperation(10), 100L),
                 Triple("double",DoubleOperation(.10), 10000),
            ).forEach {
                test("Divide long by ${it.first} ") {
                    DivideOperation(LongOperation(1000), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10, 10, 10,10, 10)))
            ).forEach {
                test("Divide long by ${it.first} throws") {
                    shouldThrow<CastException> {
                        DivideOperation(LongOperation(100), it.second)
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            listOf(
                 Triple("string",StringOperation("10"), 100.01),
                 Triple("boolean",BooleanOperation(true), 1000.1),
                 Triple("long",LongOperation(100), 10.001),
                 Triple("double",DoubleOperation(1.0), 1000.10),
            ).forEach {
                test("Divide double by ${it.first} ") {
                    DivideOperation(DoubleOperation(1000.10), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Divide double by ${it.first} throws") {
                    shouldThrow<CastException> {
                        DivideOperation(DoubleOperation(100.10), it.second)
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            listOf(
                Pair("String",StringOperation("1000")),
                Pair("boolean",BooleanOperation(true)),
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Divide  ${it.first} from double throws") {
                    shouldThrow<IncompatibleTypeException> {
                        DivideOperation( it.second, StringOperation("test"))
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            test("Not handled") {
                val op = mock(Operation::class.java)
                whenever(op.execute(any())).thenReturn(10)
                shouldThrow<IncompatibleTypeException> {
                    DivideOperation( op, StringOperation("test"))
                        .execute(mock(io.github.markgregg.expression.Context::class.java))
                }
            }

        }
    }

}
