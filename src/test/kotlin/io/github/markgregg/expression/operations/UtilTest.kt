package io.github.markgregg.expression.operations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.exceptions.CastException
import io.github.markgregg.expression.operations.Util.castToType
import io.github.markgregg.expression.operations.Util.convertToScriptType
import io.github.markgregg.expression.operations.Util.getJsonValue
import java.time.LocalDate
import java.time.LocalDateTime

class UtilTest : FunSpec() {

    init {
        context("Get Json Value") {
            val objectNode = jacksonObjectMapper().readTree(
                "{\"string\":\"test\",\"long\":214748364700,\"double\":214748364700.0,\"float\":1000.0,\"boolean\":true,\"short\":1000,\"int\":9966645}"
            )
            listOf(
                Triple("objectNode", objectNode, objectNode),
                Triple("long", objectNode.get("long"), 214748364700L),
                Triple("short", objectNode.get("short"), 1000L),
                Triple("int", objectNode.get("int"), 9966645L),
                Triple("boolean", objectNode.get("boolean"), true),
                Triple("float", objectNode.get("float"), 1000.0),
                Triple("double", objectNode.get("double"), 214748364700.0),
                Triple("string", objectNode.get("string"), "test"),
            ).forEach {
                test("Get Json type ${it.first}") {
                    getJsonValue(it.second) shouldBe it.third
                }
            }
        }

        context("Convert To Script Type") {
            listOf(
                Triple("long", 214748364700L, 214748364700L),
                Triple("short", (1000).toShort(), 1000L),
                Triple("byte", (100).toByte(), 100L),
                Triple("char", 'a', "a"),
                Triple("int", 9966645, 9966645L),
                Triple("boolean", true, true),
                Triple("float", 1000.0F, 1000.0),
                Triple("double", 214748364700.0, 214748364700.0),
                Triple("string", "test","test")
            ).forEach {
                test("Convert ${it.first} to script type") {
                    convertToScriptType(it.second) shouldBe it.third
                }
            }
        }

        context("Cast to String") {
            listOf(
                Triple("string", "test", "test"),
                Triple("boolean", true, "true"),
                Triple("long", 1000, "1000"),
                Triple("double", 100.10, "100.1"),
                Triple("date", LocalDate.of(2022, 10, 10), "2022-10-10"),
                Triple(
                    "datetime",
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                    "2022-10-10T10:10:10"
                ),
            ).forEach {
                test("Cast ${it.first} to string") {
                    castToType(it.second, String::class.java) shouldBe it.third
                }
            }
        }

        context("Cast to Long") {
            listOf(
                Triple("string", "1000", 1000),
                Triple("boolean", true, 1),
                Triple("long", 1000L, 1000L),
                Triple("double", 100.10, 100)
            ).forEach {
                test("Cast ${it.first} to long") {
                    castToType(it.second, Long::class.java) shouldBe it.third
                }
            }
        }

        context("Cast to Double") {
            listOf(
                Triple("string", "1000.0", 1000.0),
                Triple("boolean", true, 1.0),
                Triple("long", 1000L, 1000.0),
                Triple("double", 100.10, 100.10)
            ).forEach {
                test("Cast ${it.first} to double") {
                    castToType(it.second, Double::class.java) shouldBe it.third
                }
            }
        }

        context("Cast to Boolean") {
            listOf(
                Triple("string", "false", false),
                Triple("boolean", true, true),
                Triple("long", 1L, true),
                Triple("double", 1.0, true)
            ).forEach {
                test("Cast ${it.first} to boolean") {
                    castToType(it.second, Boolean::class.java) shouldBe it.third
                }
            }
        }

        context("Cast to Date") {
            listOf(
                Triple("string", "2022-10-10", LocalDate.of(2022, 10, 10)),
                Triple("date", LocalDate.of(2022, 10, 10), LocalDate.of(2022, 10, 10)),
                Triple(
                    "datetime",
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                    LocalDate.of(2022, 10, 10)
                )
            ).forEach {
                test("Cast ${it.first} to date") {
                    castToType(it.second, LocalDate::class.java) shouldBe it.third
                }
            }
        }

        context("Cast to Datetime") {
            listOf(
                Triple("string", "2022-10-10T10:10:10", LocalDateTime.of(2022, 10, 10, 10, 10, 10)),
                Triple("date", LocalDate.of(2022, 10, 10), LocalDateTime.of(2022, 10, 10, 0, 0, 0)),
                Triple(
                    "datetime",
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10)
                )
            ).forEach {
                test("Cast ${it.first} to datetime") {
                    castToType(it.second, LocalDateTime::class.java) shouldBe it.third
                }
            }
        }

        context("Cast not handled") {
            listOf(
                Triple("Date to boolean", LocalDate.of(2010,10,10), Boolean::class.java),
                Triple("Date to long", LocalDate.of(2010,10,10), Long::class.java),
                Triple("Date to double", LocalDate.of(2010,10,10), Double::class.java),
                Triple("Boolean to date", true, LocalDate::class.java),
                Triple("Boolean to datetime", true, LocalDateTime::class.java)
            ).forEach {
                test("Cast ${it.first}") {
                    shouldThrow<CastException> {
                        castToType(it.second, it.third)
                    }
                }
            }
        }

    }
}