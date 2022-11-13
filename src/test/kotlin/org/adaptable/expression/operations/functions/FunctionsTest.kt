package org.adaptable.expression.operations.functions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class FunctionsTest : FunSpec() {

    init {
        test("left String") {
            Functions.left(listOf("String", 4L)) shouldBe "Stri"
        }
        test("left non String") {
            Functions.left(listOf(12345,4L)) shouldBe "1234"
        }
        test("Right String") {
            Functions.right(listOf("String", 4L)) shouldBe "ring"
        }
        test("Right non String") {
            Functions.right(listOf(12345,4L)) shouldBe "2345"
        }
        test("Substring String") {
            Functions.substring(listOf("String",2L, 4L)) shouldBe "ri"
        }
        test("Substring non String") {
            Functions.substring(listOf(12345,2L, 4L)) shouldBe "34"
        }
        test("Lowercase String") {
            Functions.lowercase(listOf("HeLlo",)) shouldBe "hello"
        }
        test("Lowercase non String") {
            Functions.lowercase(listOf(Test("teST"),)) shouldBe "test(field=test)"
        }
        test("Uppercase String") {
            Functions.uppercase(listOf("HeLlo",)) shouldBe "HELLO"
        }
        test("Uppercase non String") {
            Functions.uppercase(listOf(Test("teST"),)) shouldBe "TEST(FIELD=TEST)"
        }
        test("Trim String") {
            Functions.trim(listOf("  Hello  ",)) shouldBe "Hello"
        }
        test("Trim non String") {
            Functions.trim(listOf(Test("test"),)) shouldBe "Test(field=test)"
        }
        test("TrimStart String") {
            Functions.trimStart(listOf("  Hello  ",)) shouldBe "Hello  "
        }
        test("TrimStart non String") {
            Functions.trimStart(listOf(Test("test"),)) shouldBe "Test(field=test)"
        }
        test("TrimEnd String") {
            Functions.trimEnd(listOf("  Hello  ",)) shouldBe "  Hello"
        }
        test("TrimEnd non String") {
            Functions.trimEnd(listOf(Test("test"),)) shouldBe "Test(field=test)"
        }
        test("tostring") {
            Functions.toString(listOf(1234.10,)) shouldBe "1234.1"
        }
        test("tolong") {
            Functions.toLong(listOf(1234.10,)) shouldBe 1234
        }
        test("todouble") {
            Functions.toDouble(listOf("1234.10",)) shouldBe 1234.10
        }
        test("toboolean") {
            Functions.toBoolean(listOf("true",)) shouldBe true
        }
        test("todate") {
            Functions.toDate(listOf("2022-10-10",)) shouldBe LocalDate.of(2022,10,10)
        }
        test("todatetime") {
            Functions.toDateTime(listOf("2022-10-10T10:10:10",)) shouldBe LocalDateTime.of(2022,10,10,10,10,10)
        }
        test("getdate") {
            Functions.getDate() shouldBe LocalDate.now()
        }
        test("getdatetime") {

            (Functions.getDateTime() as LocalDateTime).toLocalDate() shouldBe LocalDate.now()
        }
    }

    data class Test(
        val field: String
    )
}