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

class AddOperationTest : FunSpec() {

    init {
        context("Add strings") {

            listOf(
                Triple("string", StringOperation("a string"), "test a string"),
                Triple("boolean", BooleanOperation(true), "test true"),
                Triple("long", LongOperation(1000), "test 1000"),
                Triple("double", DoubleOperation(100.10), "test 100.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "test 2022-10-10"),
                Triple("datetime", DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10)), "test 2022-10-10T10:10:10"),
            ).forEach {
                test("Add ${it.first} to string") {
                    AddOperation(StringOperation("test "), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                 Triple("string",StringOperation("1000"), 1100L),
                 Triple("boolean",BooleanOperation(true), 101L),
                 Triple("long",LongOperation(1000), 1100L),
                 Triple("double",DoubleOperation(100.10), 200.1),
            ).forEach {
                test("Add ${it.first} to long") {
                    AddOperation(LongOperation(100), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10, 10, 10,10, 10)))
            ).forEach {
                test("Add ${it.first} to long throws") {
                    shouldThrow<CastException> {
                        AddOperation(LongOperation(100), it.second)
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            listOf(
                 Triple("string",StringOperation("10.11"), 110.21),
                 Triple("boolean",BooleanOperation(true), 101.10),
                 Triple("long",LongOperation(1000), 1100.10),
                 Triple("double",DoubleOperation(100.10), 200.20),
            ).forEach {
                test("Add ${it.first} to double") {
                    AddOperation(DoubleOperation(100.10), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Add ${it.first} to double throws") {
                    shouldThrow<CastException> {
                        AddOperation(DoubleOperation(100.10), it.second)
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            listOf(
                Pair("boolean",BooleanOperation(true)),
                Pair("date",DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime",DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Add to ${it.first} throws") {
                    shouldThrow<IncompatibleTypeException> {
                        AddOperation( it.second, StringOperation("test"))
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            test("Not handled") {
                val op = mock(Operation::class.java)
                whenever(op.execute(any())).thenReturn(10)
                shouldThrow<IncompatibleTypeException> {
                    AddOperation( op, StringOperation("test"))
                        .execute(mock(io.github.markgregg.expression.Context::class.java))
                }
            }

        }
    }

}
