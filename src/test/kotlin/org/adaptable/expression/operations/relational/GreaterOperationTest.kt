package org.adaptable.expression.operations.relational

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.exceptions.IncompatibleTypeException
import org.adaptable.expression.operations.types.*
import org.mockito.Mockito.mock
import java.time.LocalDate
import java.time.LocalDateTime

class GreaterOperationTest  : FunSpec() {

    init {
        context("Greater strings positive") {
            listOf(
                Triple("string", StringOperation("test"), "zesa"),
                Triple("boolean", BooleanOperation(false), "true"),
                Triple("long", LongOperation(1000), "2000"),
                Triple("double", DoubleOperation(100.10), "200.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "3022-10-10"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    "3022-10-10T10:10:10"
                ),
            ).forEach {
                test("Greater ${it.first} with string") {
                    GreaterOperation(StringOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater strings negative") {
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
                test("Greater ${it.first} with string") {
                    GreaterOperation(StringOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater long positive") {
            listOf(
                Triple("string", StringOperation("1000"), 2000L),
                Triple("boolean", BooleanOperation(false), 1L),
                Triple("long", LongOperation(1000), 2000L),
                Triple("double", DoubleOperation(100.10), 200L)
            ).forEach {
                test("Greater ${it.first} with long") {
                    GreaterOperation(LongOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater long negative") {
            listOf(
                Triple("string", StringOperation("1000"), 100L),
                Triple("boolean", BooleanOperation(true), 0L),
                Triple("long", LongOperation(1000), 100L),
                Triple("double", DoubleOperation(100.10), 10L)
            ).forEach {
                test("Greater ${it.first} with long") {
                    GreaterOperation(LongOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater double positive") {
            listOf(
                Triple("string", StringOperation("1000.0"), 2000.0),
                Triple("boolean", BooleanOperation(false), 1.0),
                Triple("long", LongOperation(1000), 2000.0),
                Triple("double", DoubleOperation(100.10), 200.10)
            ).forEach {
                test("Greater ${it.first} with double") {
                    GreaterOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater double negative") {
            listOf(
                Triple("string", StringOperation("1000"), 100.0),
                Triple("boolean", BooleanOperation(true), 0.0),
                Triple("long", LongOperation(1000), 100.0),
                Triple("double", DoubleOperation(100.10), 20.0)
            ).forEach {
                test("Greater ${it.first} with double") {
                    GreaterOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater date positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2023, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2023, 10, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2023, 10, 10)
                ),
            ).forEach {
                test("Greater ${it.first} with date") {
                    GreaterOperation(DateOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater date negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2002, 11, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2002, 11, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2002, 11, 10)
                ),
            ).forEach {
                test("Greater ${it.first} with date") {
                    GreaterOperation(DateOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater datetime positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(3022, 10, 10, 10, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(3022, 10, 10, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(3022, 10, 10, 10, 10, 10)
                )
            ).forEach {
                test("Greater ${it.first} with date") {
                    GreaterOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Greater datetime negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(2002, 10, 10, 10, 12, 0)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(2002, 10, 11, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(2002, 10, 12, 10, 10, 20)
                )
            ).forEach {
                test("Greater ${it.first} with date") {
                    GreaterOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Greater not handled") {
            listOf(
                Pair("boolean", false )
            ).forEach {
                test("Greater ${it.first} not handled") {
                    shouldThrow<IncompatibleTypeException> {
                        GreaterOperation(BooleanOperation(it.second), BooleanOperation(it.second))
                            .execute(mock(Context::class.java))
                    }
                }
            }
        }
    }
}
