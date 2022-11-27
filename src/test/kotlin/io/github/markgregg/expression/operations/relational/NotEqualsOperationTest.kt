package io.github.markgregg.expression.operations.relational

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.types.*
import org.mockito.Mockito.mock
import java.time.LocalDate
import java.time.LocalDateTime

class NotNotEqualsOperationTest  : FunSpec() {

    init {
        context("NotEqual strings positive") {
            listOf(
                Triple("string", StringOperation("test"), "test2"),
                Triple("boolean", BooleanOperation(true), "false"),
                Triple("long", LongOperation(1000), "100"),
                Triple("double", DoubleOperation(100.10), "10.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "2012-10-10"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    "2012-10-10T10:10:10"
                ),
            ).forEach {
                test("NotNotEquals ${it.first} with string") {
                    NotEqualsOperation(StringOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("NotEqual strings negative") {
            listOf(
                Triple("string", StringOperation("test"), "test"),
                Triple("boolean", BooleanOperation(true), "true"),
                Triple("long", LongOperation(1000), "1000"),
                Triple("double", DoubleOperation(100.10), "100.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "2022-10-10"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    "2022-10-10T10:10:10"
                ),
            ).forEach {
                test("NotNotEquals ${it.first} with string") {
                    NotEqualsOperation(StringOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("NotEqual long positive") {
            listOf(
                Triple("string", StringOperation("1000"), 100L),
                Triple("boolean", BooleanOperation(true), 0L),
                Triple("long", LongOperation(1000), 100L),
                Triple("double", DoubleOperation(100.10), 10L)
            ).forEach {
                test("NotNotEquals ${it.first} with long") {
                    NotEqualsOperation(LongOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("NotEqual long negative") {
            listOf(
                Triple("string", StringOperation("1000"), 1000L),
                Triple("boolean", BooleanOperation(true), 1L),
                Triple("long", LongOperation(1000), 1000L),
                Triple("double", DoubleOperation(100.10), 100L)
            ).forEach {
                test("NotNotEquals ${it.first} with long") {
                    NotEqualsOperation(LongOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("NotEqual double positive") {
            listOf(
                Triple("string", StringOperation("1000.0"), 2000.0),
                Triple("boolean", BooleanOperation(true), 0.0),
                Triple("long", LongOperation(1000), 2000.0),
                Triple("double", DoubleOperation(100.10), 20.10)
            ).forEach {
                test("NotNotEquals ${it.first} with double") {
                    NotEqualsOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("NotEqual double negative") {
            listOf(
                Triple("string", StringOperation("1000"), 1000.0),
                Triple("boolean", BooleanOperation(true), 1.0),
                Triple("long", LongOperation(1000), 1000.0),
                Triple("double", DoubleOperation(100.10), 100.10)
            ).forEach {
                test("NotNotEquals ${it.first} with double") {
                    NotEqualsOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("NotEqual boolean positive") {
            listOf(
                Triple("string", StringOperation("true"), false),
                Triple("boolean", BooleanOperation(true), false),
                Triple("long", LongOperation(1), false),
                Triple("double", DoubleOperation(1.0), false)
            ).forEach {
                test("NotNotEquals ${it.first} with boolean") {
                    NotEqualsOperation(BooleanOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("NotEqual boolean negative") {
            listOf(
                Triple("string", StringOperation("false"), false),
                Triple("boolean", BooleanOperation(false), false),
                Triple("long", LongOperation(0), false),
                Triple("double", DoubleOperation(0.0), false)
            ).forEach {
                test("NotNotEquals ${it.first} with boolean") {
                    NotEqualsOperation(BooleanOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("NotEqual date positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2012, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2012, 10, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2012, 10, 10)
                ),
            ).forEach {
                test("NotNotEquals ${it.first} with date") {
                    NotEqualsOperation(DateOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("NotEqual date negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2022, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2022, 10, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2022, 10, 10)
                ),
            ).forEach {
                test("NotNotEquals ${it.first} with date") {
                    NotEqualsOperation(DateOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }

        context("NotEqual datetime positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(2012, 10, 10, 10, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(2012, 10, 10, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(2012, 10, 10, 10, 10, 10)
                )
            ).forEach {
                test("NotNotEquals ${it.first} with date") {
                    NotEqualsOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
                }
            }
        }

        context("NotEqual datetime negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(2022, 10, 10, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10)
                )
            ).forEach {
                test("NotNotEquals ${it.first} with date") {
                    NotEqualsOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
                }
            }
        }
    }
}
