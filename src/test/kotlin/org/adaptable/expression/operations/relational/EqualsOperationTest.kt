package org.adaptable.expression.operations.relational

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.operations.types.*
import org.mockito.Mockito.mock
import java.time.LocalDate
import java.time.LocalDateTime

class EqualsOperationTest  : FunSpec() {

    init {
        context("Equal strings positive") {
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
                test("Equals ${it.first} with string") {
                    EqualsOperation(StringOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Equal strings negative") {
            listOf(
                Triple("string", StringOperation("test"), "test2"),
                Triple("boolean", BooleanOperation(true), "false"),
                Triple("long", LongOperation(1000), "2000"),
                Triple("double", DoubleOperation(100.10), "200.1"),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), "2022-11-01"),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    "2022-10-10T23:10:20"
                ),
            ).forEach {
                test("Equals ${it.first} with string") {
                    EqualsOperation(StringOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Equal long positive") {
            listOf(
                Triple("string", StringOperation("1000"), 1000L),
                Triple("boolean", BooleanOperation(true), 1L),
                Triple("long", LongOperation(1000), 1000L),
                Triple("double", DoubleOperation(100.10), 100L)
            ).forEach {
                test("Equals ${it.first} with long") {
                    EqualsOperation(LongOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Equal long negative") {
            listOf(
                Triple("string", StringOperation("1000"), 2000L),
                Triple("boolean", BooleanOperation(true), 0L),
                Triple("long", LongOperation(1000), 2000L),
                Triple("double", DoubleOperation(100.10), 200L)
            ).forEach {
                test("Equals ${it.first} with long") {
                    EqualsOperation(LongOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Equal double positive") {
            listOf(
                Triple("string", StringOperation("1000.0"), 1000.0),
                Triple("boolean", BooleanOperation(true), 1.0),
                Triple("long", LongOperation(1000), 1000.0),
                Triple("double", DoubleOperation(100.10), 100.10)
            ).forEach {
                test("Equals ${it.first} with double") {
                    EqualsOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Equal double negative") {
            listOf(
                Triple("string", StringOperation("1000"), 2000.0),
                Triple("boolean", BooleanOperation(true), 0.0),
                Triple("long", LongOperation(1000), 2000.0),
                Triple("double", DoubleOperation(100.10), 200.0)
            ).forEach {
                test("Equals ${it.first} with double") {
                    EqualsOperation(DoubleOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Equal boolean positive") {
            listOf(
                Triple("string", StringOperation("true"), true),
                Triple("boolean", BooleanOperation(true), true),
                Triple("long", LongOperation(1), true),
                Triple("double", DoubleOperation(1.0), true)
            ).forEach {
                test("Equals ${it.first} with boolean") {
                    EqualsOperation(BooleanOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Equal boolean negative") {
            listOf(
                Triple("string", StringOperation("false"), true),
                Triple("boolean", BooleanOperation(false), true),
                Triple("long", LongOperation(0), true),
                Triple("double", DoubleOperation(0.0), true)
            ).forEach {
                test("Equals ${it.first} with boolean") {
                    EqualsOperation(BooleanOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Equal date positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2022, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2022, 10, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2022, 10, 10)
                ),
            ).forEach {
                test("Equals ${it.first} with date") {
                    EqualsOperation(DateOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Equal date negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10"), LocalDate.of(2022, 11, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDate.of(2022, 11, 10)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDate.of(2022, 11, 10)
                ),
            ).forEach {
                test("Equals ${it.first} with date") {
                    EqualsOperation(DateOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }

        context("Equal datetime positive") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(2022, 10, 10, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10)
                )
            ).forEach {
                test("Equals ${it.first} with date") {
                    EqualsOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe true
                }
            }
        }

        context("Equal datetime negative") {
            listOf(
                Triple("string", StringOperation("2022-10-10T10:10:10"), LocalDateTime.of(2022, 10, 10, 10, 12, 0)),
                Triple("date", DateOperation(LocalDate.of(2022, 10, 10)), LocalDateTime.of(2022, 10, 11, 0, 0, 0)),
                Triple(
                    "datetime",
                    DateTimeOperation(LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                    LocalDateTime.of(2022, 10, 12, 10, 10, 20)
                )
            ).forEach {
                test("Equals ${it.first} with date") {
                    EqualsOperation(DateTimeOperation(it.third), it.second)
                        .execute(mock(Context::class.java)) shouldBe false
                }
            }
        }
    }
}
