package io.github.markgregg.expression

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.exceptions.InvalidDateException
import io.github.markgregg.expression.exceptions.MissingSymbolException
import io.github.markgregg.expression.exceptions.ParseException
import io.github.markgregg.expression.exceptions.UnknownFunctionException
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime

class ExpressionParserTest : FunSpec() {

    init {
        test("Parse string") {
            Util.parser.parse(Util.tokeniser.tokenise("'string'")).execute(mock(Context::class.java)) shouldBe "string"
        }

        test("Parse long") {
            Util.parser.parse(Util.tokeniser.tokenise("10000")).execute(mock(Context::class.java)) shouldBe 10000L
        }

        test("Parse double") {
            Util.parser.parse(Util.tokeniser.tokenise("100.10")).execute(mock(Context::class.java)) shouldBe 100.10
        }

        test("Parse boolean") {
            Util.parser.parse(Util.tokeniser.tokenise("true")).execute(mock(Context::class.java)) shouldBe true
        }

        test("Add string and long") {
            Util.parser.parse(Util.tokeniser.tokenise("'100' + 100")).execute(mock(Context::class.java)) shouldBe "100100"
        }

        test("Add string and long2") {
            Util.parser.parse(Util.tokeniser.tokenise("100 + '100'")).execute(mock(Context::class.java)) shouldBe 200L
        }

        test("Add string and double") {
            Util.parser.parse(Util.tokeniser.tokenise("'100.1' + 100.1")).execute(mock(Context::class.java)) shouldBe "100.1100.1"
        }

        test("Add string and double 2") {
            Util.parser.parse(Util.tokeniser.tokenise("100.1 + '100.1'")).execute(mock(Context::class.java)) shouldBe 200.2
        }

        test("Add long and double") {
            Util.parser.parse(Util.tokeniser.tokenise("100 + 100.1")).execute(mock(Context::class.java)) shouldBe 200.1
        }

        test("Add long and double 2") {
            Util.parser.parse(Util.tokeniser.tokenise("100.1 + 100")).execute(mock(Context::class.java)) shouldBe 200.1
        }

        test("Invalid date") {
            shouldThrow<InvalidDateException> {
                Util.parser.parse(Util.tokeniser.tokenise("#20210-10#")).execute(mock(Context::class.java)) shouldBe LocalDate.of(2022,10,10)
            }
        }

        test("Parse date") {
            Util.parser.parse(Util.tokeniser.tokenise("#2022-10-10#")).execute(mock(Context::class.java)) shouldBe LocalDate.of(2022,10,10)
        }

        test("Parse datetime") {
            Util.parser.parse(Util.tokeniser.tokenise("#2022-10-10T10:10:10#")).execute(mock(Context::class.java)) shouldBe LocalDateTime.of(2022,10,10,10,10,10)
        }

        test("When Function can't be found throws Exception") {
            shouldThrow<MissingSymbolException> {
                Util.parser.parse(Util.tokeniser.tokenise("substr('test string',5,11")).execute(mock(Context::class.java))
            }
        }

        test("Recognised Function throws Exception") {
            shouldThrow<UnknownFunctionException> {
                Util.parser.parse(Util.tokeniser.tokenise("subdstr()")).execute(mock(Context::class.java))
            }
        }
        test("Function without close brackets throws Exception") {
            shouldThrow<MissingSymbolException> {
                Util.parser.parse(Util.tokeniser.tokenise("substr('test string',5,11")).execute(mock(Context::class.java))
            }
        }

        test("Function with incorrect arguments throws an Exception") {
            shouldThrow<MissingSymbolException> {
                Util.parser.parse(Util.tokeniser.tokenise("substr('test string',5)")).execute(mock(Context::class.java))
            }
        }

        test("Function without open bracket throws an Exception") {
            shouldThrow<MissingSymbolException> {
                Util.parser.parse(Util.tokeniser.tokenise("substr 'test string',5)")).execute(mock(Context::class.java))
            }
        }

        test("Function returns correct value") {
            Util.parser.parse(Util.tokeniser.tokenise("substr('test string',5,11)")).execute(mock(Context::class.java)) shouldBe "string"
        }

        test("Parameterless Function returns correct value") {
            Util.parser.parse(Util.tokeniser.tokenise("today()")).execute(mock(Context::class.java)) shouldBe LocalDate.now()
        }

        test("Non operator returns correct value") {
            val context = mock(Context::class.java)
            whenever(context.getItem(eq("test"))).thenReturn("test")

            Util.parser.parse(Util.tokeniser.tokenise("\$test")).execute(context) shouldBe "test"
        }

        test("Unary operator returns correct value") {
            Util.parser.parse(Util.tokeniser.tokenise("!true")).execute(mock(Context::class.java)) shouldBe false
        }

        test("Unary operator with name returns correct value") {
            val context = mock(Context::class.java)
            whenever(context.getItem(eq("test"))).thenReturn(jacksonObjectMapper().readTree("{\"field1\":\"a value\"}"))

            Util.parser.parse(Util.tokeniser.tokenise("\$test.field1")).execute(context) shouldBe "a value"
        }

        test("Unary operator without target throws exception") {
            val context = mock(Context::class.java)
            whenever(context.getItem(eq("test"))).thenReturn(jacksonObjectMapper().readTree("{\"field1\":\"a value\"}"))

            shouldThrow<ParseException> {
                Util.parser.parse(Util.tokeniser.tokenise(".field1")).execute(context)
            }
        }

        test("Binary operator returns correct value") {
            Util.parser.parse(Util.tokeniser.tokenise("1234 == 1234")).execute(mock(Context::class.java)) shouldBe true
        }

        test("Precedence is correctly applied 1") {
            Util.parser.parse(Util.tokeniser.tokenise("12 + 12 == 36 - 12")).execute(mock(Context::class.java)) shouldBe true
        }

        test("Precedence is correctly applied 2") {
            Util.parser.parse(Util.tokeniser.tokenise("12 + 12 * 2")).execute(mock(Context::class.java)) shouldBe 36
        }

        test("Precedence is correctly applied 3") {
            Util.parser.parse(Util.tokeniser.tokenise("36 / 12 + 12")).execute(mock(Context::class.java)) shouldBe 15
        }

        test("Precedence is correctly applied 4") {
            Util.parser.parse(Util.tokeniser.tokenise("12 + 12 * 2 > 36 / 12 + 12")).execute(mock(Context::class.java)) shouldBe true
        }

        test("Precedence is correctly applied 5") {
            Util.parser.parse(Util.tokeniser.tokenise("!(12 + 12 * 2 > 36 / 12 + 12)")).execute(mock(Context::class.java)) shouldBe false
        }

        test("Precedence is correctly applied 6") {
            Util.parser.parse(Util.tokeniser.tokenise("true != !true")).execute(mock(Context::class.java)) shouldBe true
        }

        test("Precedence is correctly applied 7") {
            Util.parser.parse(Util.tokeniser.tokenise("(12 + 12) * 2 >= 36 + 12")).execute(mock(Context::class.java)) shouldBe true
        }

        test("Precedence is correctly applied 8") {
            Util.parser.parse(Util.tokeniser.tokenise("(12 + 12) * 2")).execute(mock(Context::class.java)) shouldBe 48
        }

        test("Conditional operator") {
            Util.parser.parse(Util.tokeniser.tokenise("12 + 12 * 2 > 36 / 12 + 12 ? 'is true' : 'is false'")).execute(mock(
                Context::class.java)) shouldBe "is true"
        }

        test("Conditional operator 2") {
            Util.parser.parse(Util.tokeniser.tokenise("12 + 12 * 2 < 36 / 12 + 12 ? 21 + 4 : 27 - 3")).execute(mock(Context::class.java)) shouldBe 24
        }

        test("test issue 1") {
            val map = mapOf("id" to 1)
            val context = mock(Context::class.java)
            whenever(context.getItem(any())).thenReturn(jacksonObjectMapper().readTree(jacksonObjectMapper().writeValueAsString(map)))
            Util.parser.parse(Util.tokeniser.tokenise("\$parameter['id']==1")).execute(context) shouldBe true
        }

        test("test issue 2") {
            val context = mock(Context::class.java)
            whenever(context.getItem(any())).thenReturn(jacksonObjectMapper().readTree("{\"name\":null,\"id\":null,\"identities\":[{\"type\":\"country\",\"code\":\"GB\"}]}"))
            Util.parser.parse(Util.tokeniser.tokenise("\$body.identities[\$type=='country'].code=='GB'")).execute(context) shouldBe true
        }

        test("test issue 3") {
            val context = mock(Context::class.java)
            whenever(context.getItem(any())).thenReturn(jacksonObjectMapper().readTree("{\"name\":\"Mark\",\"id\":null,\"identities\":[{\"type\":\"country\",\"code\":\"GB\"}]}"))
            Util.parser.parse(Util.tokeniser.tokenise("\$body.name=='Mark'")).execute(context) shouldBe true
        }



    }
}
