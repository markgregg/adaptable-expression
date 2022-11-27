package io.github.markgregg.expression.operations.relational

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.types.*
import org.mockito.Mockito.mock
import java.time.LocalDate
import java.time.LocalDateTime

class GreaterGreaterEqualsOperationTest  : FunSpec() {

    init {
        context("Greater equals strings positive") {
            listOf(
                Triple("string", StringOperation("test"), "zesa"),
                Triple("boolean", BooleanOperation(false), "true"),
                Triple("long", LongOperation(1000), "1000"),
                Triple("double", DoubleOperation(100.10), "100.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "3022-10-10"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    "3022-10-10T10:10:10"
                ),
            ).forEach {
                test("GreaterEquals ${it.first} with string") {
                    GreaterEqualsOperation(StringOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater equals strings negative") {
            listOf(
                Triple("string", StringOperation("test"), "est"),
                Triple("boolean", BooleanOperation(true), "false"),
                Triple("long", LongOperation(1000), "000"),
                Triple("double", DoubleOperation(100.10), "00.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "1022-11-01"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    "1022-10-10T23:10:20"
                ),
            ).forEach {
                test("GreaterEquals ${it.first} with string") {
                    GreaterEqualsOperation(StringOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater equals long positive") {
            listOf(
                Triple("string", StringOperation("1000"), 2000L),
                Triple("boolean", BooleanOperation(false), 1L),
                Triple("long", LongOperation(1000), 1000L),
                Triple("double", DoubleOperation(100.10), 100L)
            ).forEach {
                test("GreaterEquals ${it.first} with long") {
                    GreaterEqualsOperation(LongOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater equals long negative") {
            listOf(
                Triple("string", StringOperation("1000"), 100L),
                Triple("boolean", BooleanOperation(true), 0L),
                Triple("long", LongOperation(1000), 100L),
                Triple("double", DoubleOperation(100.10), 10L)
            ).forEach {
                test("GreaterEquals ${it.first} with long") {
                    GreaterEqualsOperation(LongOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater equals double positive") {
            listOf(
                Triple("string", StringOperation("1000.0"), 2000.0),
                Triple("boolean", BooleanOperation(false), 1.0),
                Triple("long", LongOperation(1000), 2000.0),
                Triple("double", DoubleOperation(100.10), 100.10)
            ).forEach {
                test("GreaterEquals ${it.first} with double") {
                    GreaterEqualsOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater equals double negative") {
            listOf(
                Triple("string", StringOperation("1000"), 100.0),
                Triple("boolean", BooleanOperation(true), 0.0),
                Triple("long", LongOperation(1000), 100.0),
                Triple("double", DoubleOperation(100.10), 20.0)
            ).forEach {
                test("GreaterEquals ${it.first} with double") {
                    GreaterEqualsOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater equals date positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2022, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2022, 10, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2022, 10, 10)
                ),
            ).forEach {
                test("GreaterEquals ${it.first} with date") {
                    GreaterEqualsOperation(DateOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater equals date negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2002, 11, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2002, 11, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2002, 11, 10)
                ),
            ).forEach {
                test("GreaterEquals ${it.first} with date") {
                    GreaterEqualsOperation(DateOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater equals datetime positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(3022, 10, 10, 10, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(3022, 10, 10, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(3022, 10, 10, 10, 10, 10)
                )
            ).forEach {
                test("GreaterEquals ${it.first} with date") {
                    GreaterEqualsOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater equals datetime negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(2002, 10, 10, 10, 12, 0)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(2002, 10, 11, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(2002, 10, 12, 10, 10, 20)
                )
            ).forEach {
                test("GreaterEquals ${it.first} with date") {
                    GreaterEqualsOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater equals not handled") {
            listOf(
                Pair("boolean", false )
            ).forEach {
                test("GreaterEquals ${it.first} not handled") {
                    shouldThrow<IncompatibleTypeException> {
                        GreaterEqualsOperation(BooleanOperation(it.second), BooleanOperation(it.second))
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }
        }
    }
}

