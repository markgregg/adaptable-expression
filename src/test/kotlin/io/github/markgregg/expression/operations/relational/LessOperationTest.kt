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

class LessOperationTest  : FunSpec() {

    init {
        context("Less strings positive") {
            listOf(
                Triple("string", StringOperation("zzst"), "zesa"),
                Triple("boolean", BooleanOperation(true), "false"),
                Triple("long", LongOperation(2000), "1000"),
                Triple("double", DoubleOperation(200.10), "100.1"),
                Triple("date", DateOperation(LocalDate.of(3022, 10, 10)), "2022-10-10"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(3022, 10, 10, 10, 10, 10)),
                    "2022-10-10T10:10:10"
                ),
            ).forEach {
                test("Less${it.first} with string") {
                    LessOperation(StringOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Less strings negative") {
            listOf(
                Triple("string", StringOperation("test"), "zzest"),
                Triple("boolean", BooleanOperation(false), "true"),
                Triple("long", LongOperation(1000), "4000"),
                Triple("double", DoubleOperation(100.10), "400.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "3022-11-01"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    "3022-10-10T23:10:20"
                ),
            ).forEach {
                test("Less${it.first} with string") {
                    LessOperation(StringOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Less long positive") {
            listOf(
                Triple("string", StringOperation("1000"), 100L),
                Triple("boolean", BooleanOperation(true), 0L),
                Triple("long", LongOperation(1000), 900L),
                Triple("double", DoubleOperation(100.10), 90L)
            ).forEach {
                test("Less${it.first} with long") {
                    LessOperation(LongOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Less long negative") {
            listOf(
                Triple("string", StringOperation("1000"), 4000L),
                Triple("boolean", BooleanOperation(false), 1L),
                Triple("long", LongOperation(1000), 4000L),
                Triple("double", DoubleOperation(100.10), 500L)
            ).forEach {
                test("Less${it.first} with long") {
                    LessOperation(LongOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Less double positive") {
            listOf(
                Triple("string", StringOperation("1000.0"), 100.0),
                Triple("boolean", BooleanOperation(true), 0.0),
                Triple("long", LongOperation(1000), 900.0),
                Triple("double", DoubleOperation(100.10), 30.10)
            ).forEach {
                test("Less${it.first} with double") {
                    LessOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Less double negative") {
            listOf(
                Triple("string", StringOperation("1000"), 5000.0),
                Triple("boolean", BooleanOperation(false), 1.0),
                Triple("long", LongOperation(1000), 40000.0),
                Triple("double", DoubleOperation(100.10), 2000.0)
            ).forEach {
                test("Less${it.first} with double") {
                    LessOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Less date positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(1022, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(1022, 10, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(1022, 10, 10)
                ),
            ).forEach {
                test("Less${it.first} with date") {
                    LessOperation(DateOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Less date negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(3012, 11, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(3002, 11, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(3002, 11, 10)
                ),
            ).forEach {
                test("Less${it.first} with date") {
                    LessOperation(DateOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Less datetime positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(2002, 10, 10, 10, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(2002, 10, 10, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(2002, 10, 10, 10, 10, 10)
                )
            ).forEach {
                test("Less${it.first} with date") {
                    LessOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("Less datetime negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(3022, 10, 10, 10, 12, 0)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(3022, 10, 11, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(3022, 10, 12, 10, 10, 20)
                )
            ).forEach {
                test("Less${it.first} with date") {
                    LessOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("Less not handled") {
            listOf(
                Pair("boolean", false )
            ).forEach {
                test("Less${it.first} not handled") {
                    shouldThrow<IncompatibleTypeException> {
                        LessOperation(BooleanOperation(it.second), BooleanOperation(it.second))
                            .execute(mock(io.github.markgregg.expression.Context::class.java))
                    }
                }
            }
        }
    }
}