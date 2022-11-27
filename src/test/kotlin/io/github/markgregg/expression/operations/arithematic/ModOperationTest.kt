package io.github.markgregg.expression.operations.arithematic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.CastException
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.types.*
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime


class ModOperationTest : FunSpec() {

    init {
        context("Mod") {

            listOf(
                Triple("string", StringOperation("10"), 0L),
                Triple("boolean", BooleanOperation(true), 0L),
                Triple("long", LongOperation(30), 10L),
                Triple("double", DoubleOperation(70.0), 20L),
            ).forEach {
                test("Mod long by ${it.first} ") {
                    ModOperation(LongOperation(1000), it.second)
                        .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date", DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime", DateTimeOperation(LocalDateTime.of(2022,10, 10, 10,10, 10)))
            ).forEach {
                test("Mod long by ${it.first} throws") {
                    shouldThrow<CastException> {
                        ModOperation(LongOperation(100), it.second)
                            .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            listOf(
                Triple("string", StringOperation("10"), 0.0),
                Triple("boolean", BooleanOperation(true), 0.0),
                Triple("long", LongOperation(15), 10.0),
                Triple("double", DoubleOperation(75.0), 25.0),
            ).forEach {
                test("Mod double by ${it.first} ") {
                    ModOperation(DoubleOperation(1000.0), it.second)
                        .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.third
                }
            }

            listOf(
                Pair("date", DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime", DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Mod double by ${it.first} throws") {
                    shouldThrow<CastException> {
                        ModOperation(DoubleOperation(100.10), it.second)
                            .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            listOf(
                Pair("String", StringOperation("1000")),
                Pair("boolean", BooleanOperation(true)),
                Pair("date", DateOperation(LocalDate.of(2022, 10, 10))),
                Pair("datetime", DateTimeOperation(LocalDateTime.of(2022,10,10,10,10,10))),
            ).forEach {
                test("Mod  ${it.first} from double throws") {
                    shouldThrow<IncompatibleTypeException> {
                        ModOperation( it.second, StringOperation("test"))
                            .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }

            test("Not handled") {
                val op = Mockito.mock(Operation::class.java)
                whenever(op.execute(any())).thenReturn(10)
                shouldThrow<IncompatibleTypeException> {
                    ModOperation( op, StringOperation("test"))
                        .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java))
                }
            }

        }
    }

}
