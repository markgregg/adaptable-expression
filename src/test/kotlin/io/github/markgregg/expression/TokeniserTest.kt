package io.github.markgregg.expression

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.exceptions.UnknownElementException

internal class TokeniserTest : FunSpec() {

    init {
        test("String token") {
            val results = Util.tokeniser.tokenise("'this is a string 3432432 @@?@<>$%£^%$£%'")
            results shouldBe listOf(Token(TokenType.String, "this is a string 3432432 @@?@<>$%£^%$£%", null, 0, 40))
        }

        test("Long token") {
            val results = Util.tokeniser.tokenise("12321323344")
            results shouldBe listOf(Token(TokenType.Long, "12321323344", null,0, 10))
        }

        test("Minus Long token") {
            val results = Util.tokeniser.tokenise("-12321323344")
            results shouldBe listOf(Token(TokenType.Long, "-12321323344",null, 0, 11))
        }

        test("Double token") {
            val results = Util.tokeniser.tokenise(".23344")
            results shouldBe listOf(Token(TokenType.Double, ".23344",null, 0, 5))
        }

        test("Double token 2") {
            val results = Util.tokeniser.tokenise("55.23344")
            results shouldBe listOf(Token(TokenType.Double, "55.23344",null, 0, 7))
        }

        test("Minus Double token") {
            val results = Util.tokeniser.tokenise("-56.23344")
            results shouldBe listOf(Token(TokenType.Double, "-56.23344",null, 0, 8))
        }

        test("Minus Double token 2") {
            val results = Util.tokeniser.tokenise("-.23344")
            results shouldBe listOf(Token(TokenType.Double, "-.23344",null, 0, 6))
        }


        test("String token with space") {
            val results = Util.tokeniser.tokenise("  'this is a string 3432432 @@?@<>$%£^%$£%'  ")
            results shouldBe listOf(Token(TokenType.String, "this is a string 3432432 @@?@<>$%£^%$£%", null, 2, 42))
        }

        test("Long token with space") {
            val results = Util.tokeniser.tokenise("  12321323344  ")
            results shouldBe listOf(Token(TokenType.Long, "12321323344",null, 2, 12))
        }

        test("Minus Long token with space") {
            val results = Util.tokeniser.tokenise("  -12321323344  ")
            results shouldBe listOf(Token(TokenType.Long, "-12321323344",null, 2, 13))
        }

        test("Double token with space") {
            val results = Util.tokeniser.tokenise("  .23344  ")
            results shouldBe listOf(Token(TokenType.Double, ".23344",null, 2, 7))
        }

        test("Double token 2 with space") {
            val results = Util.tokeniser.tokenise("  55.23344 ")
            results shouldBe listOf(Token(TokenType.Double, "55.23344",null, 2, 9))
        }

        test("Minus Double token with space") {
            val results = Util.tokeniser.tokenise(" -56.23344  ")
            results shouldBe listOf(Token(TokenType.Double, "-56.23344", null, 1, 9))
        }

        test("Minus Double token 2 with space") {
            val results = Util.tokeniser.tokenise(" -.23344 ")
            results shouldBe listOf(Token(TokenType.Double, "-.23344", null, 1, 7))
        }

        test("Date") {
            val results = Util.tokeniser.tokenise("#2022-02-12#")
            results shouldBe listOf(Token(TokenType.DateTime, "2022-02-12",null, 0, 11))
        }

        test("Date with space") {
            val results = Util.tokeniser.tokenise("  #2022-02-12#  ")
            results shouldBe listOf(Token(TokenType.DateTime, "2022-02-12",null, 2, 13))
        }

        test("False") {
            val results = Util.tokeniser.tokenise("false")
            results shouldBe listOf(Token(TokenType.Boolean, "false",null, 0, 4))
        }

        test("False with space") {
            val results = Util.tokeniser.tokenise("  false  ")
            results shouldBe listOf(Token(TokenType.Boolean, "false",null, 2, 6))
        }

        test("True") {
            val results = Util.tokeniser.tokenise("true")
            results shouldBe listOf(Token(TokenType.Boolean, "true",null, 0, 3))
        }

        test("True with space") {
            val results = Util.tokeniser.tokenise("  true  ")
            results shouldBe listOf(Token(TokenType.Boolean, "true",null, 2, 5))
        }

        test("Name") {
            val results = Util.tokeniser.tokenise("field")
            results shouldBe listOf(Token(TokenType.Name, "field",null, 0, 4))
        }

        test("Name with space") {
            val results = Util.tokeniser.tokenise("  field"  )
            results shouldBe listOf(Token(TokenType.Name, "field",null, 2, 6))
        }

        test("Field") {
            val results = Util.tokeniser.tokenise("."  )
            results shouldBe listOf(Token(TokenType.Operation, ".",Util.safeConfig.getSingleElement('.'), 0, 0))
        }

        test("Minus") {
            val results = Util.tokeniser.tokenise("-"  )
            results shouldBe listOf(Token(TokenType.Operation, "-",Util.safeConfig.getSingleElement('-'), 0, 0))
        }

        test("Plus") {
            val results = Util.tokeniser.tokenise("+"  )
            results shouldBe listOf(Token(TokenType.Operation, "+",Util.safeConfig.getSingleElement('+'), 0, 0))
        }

        test("Divide") {
            val results = Util.tokeniser.tokenise("/"  )
            results shouldBe listOf(Token(TokenType.Operation, "/",Util.safeConfig.getSingleElement('/'), 0, 0))
        }

        test("Multiply") {
            val results = Util.tokeniser.tokenise("*"  )
            results shouldBe listOf(Token(TokenType.Operation, "*",Util.safeConfig.getSingleElement('*'), 0, 0))
        }

        test("Mod") {
            val results = Util.tokeniser.tokenise("%"  )
            results shouldBe listOf(Token(TokenType.Operation, "%",Util.safeConfig.getSingleElement('%'), 0, 0))
        }

        test("Equals") {
            val results = Util.tokeniser.tokenise("=="  )
            results shouldBe listOf(Token(TokenType.Operation, "==",Util.safeConfig.getDualElement("=="), 0, 1))
        }

        test("Not") {
            val results = Util.tokeniser.tokenise("!"  )
            results shouldBe listOf(Token(TokenType.Operation, "!",Util.safeConfig.getSingleElement('!'), 0, 0))
        }

        test("Not Equals") {
            val results = Util.tokeniser.tokenise("!="  )
            results shouldBe listOf(Token(TokenType.Operation, "!=",Util.safeConfig.getDualElement("!="), 0, 1))
        }

        test("Greater") {
            val results = Util.tokeniser.tokenise(">"  )
            results shouldBe listOf(Token(TokenType.Operation, ">",Util.safeConfig.getSingleElement('>'), 0, 0))
        }

        test("Greater Equals") {
            val results = Util.tokeniser.tokenise(">="  )
            results shouldBe listOf(Token(TokenType.Operation, ">=",Util.safeConfig.getDualElement(">="), 0, 1))
        }

        test("Less") {
            val results = Util.tokeniser.tokenise("<"  )
            results shouldBe listOf(Token(TokenType.Operation, "<",Util.safeConfig.getSingleElement('<'), 0, 0))
        }

        test("Less Equals") {
            val results = Util.tokeniser.tokenise("<="  )
            results shouldBe listOf(Token(TokenType.Operation, "<=",Util.safeConfig.getDualElement("<="), 0, 1))
        }

        test("Context") {
            val results = Util.tokeniser.tokenise("\$field"  )
            results shouldBe listOf(
                Token(TokenType.Operation, "$", Util.safeConfig.getSingleElement('$'), 0, 0),
                Token(TokenType.Name, "field", null, 1, 5)
            )
        }

        test("Open brackets") {
            val results = Util.tokeniser.tokenise("("  )
            results shouldBe listOf(Token(TokenType.Support, "(", null,0, 0))
        }

        test("Close brackets") {
            val results = Util.tokeniser.tokenise(")"  )
            results shouldBe listOf(Token(TokenType.Support, ")", null, 0, 0))
        }

        test("Open Array") {
            val results = Util.tokeniser.tokenise("["  )
            results shouldBe listOf(Token(TokenType.Operation, "[", Util.safeConfig.getSingleElement('['), 0, 0))
        }

        test("Close Array") {
            val results = Util.tokeniser.tokenise("]"  )
            results shouldBe listOf(Token(TokenType.Operation, "]", Util.safeConfig.getSingleElement(']'), 0, 0))
        }

        test("Complex 1") {
            val results = Util.tokeniser.tokenise("field.field2[field3=='value'].field4 == 12213"  )
            results shouldBe listOf(
                Token(TokenType.Name, "field",null, 0, 4),
                Token(TokenType.Operation, ".", Util.safeConfig.getSingleElement('.'),5, 5),
                Token(TokenType.Name, "field2", null,6, 11),
                Token(TokenType.Operation, "[", Util.safeConfig.getSingleElement('['),12, 12),
                Token(TokenType.Name, "field3", null, 13, 18),
                Token(TokenType.Operation, "==", Util.safeConfig.getDualElement("=="),19, 20),
                Token(TokenType.String, "value", null,21, 27),
                Token(TokenType.Operation, "]", Util.safeConfig.getSingleElement(']'),28, 28),
                Token(TokenType.Operation, ".", Util.safeConfig.getSingleElement('.'),29, 29),
                Token(TokenType.Name, "field4", null,30, 35),
                Token(TokenType.Operation, "==", Util.safeConfig.getDualElement("=="),37, 38),
                Token(TokenType.Long, "12213", null, 40, 44)
            )
        }

        test("Complex 2") {
            val results = Util.tokeniser.tokenise("field.field2[field3=='value'] > field4 + field5"  )
            results shouldBe listOf(
                Token(TokenType.Name, "field",null, 0, 4),
                Token(TokenType.Operation, ".", Util.safeConfig.getSingleElement('.'),5, 5),
                Token(TokenType.Name, "field2", null,6, 11),
                Token(TokenType.Operation, "[", Util.safeConfig.getSingleElement('['),12, 12),
                Token(TokenType.Name, "field3", null,13, 18),
                Token(TokenType.Operation, "==", Util.safeConfig.getDualElement("=="),19, 20),
                Token(TokenType.String, "value", null,21, 27),
                Token(TokenType.Operation, "]", Util.safeConfig.getSingleElement(']'),28, 28),
                Token(TokenType.Operation, ">", Util.safeConfig.getSingleElement('>'),30, 30),
                Token(TokenType.Name, "field4", null,32, 37),
                Token(TokenType.Operation, "+", Util.safeConfig.getSingleElement('+'),39, 39),
                Token(TokenType.Name, "field5", null, 41, 46)
            )
        }

        test("Function with parameters") {
            val results = Util.tokeniser.tokenise("function(12+3,'fred',field)" )
            results shouldBe listOf(
                Token(TokenType.Name, "function",null, 0, 7),
                Token(TokenType.Support, "(", null,8, 8),
                Token(TokenType.Long, "12",  null,9, 10),
                Token(TokenType.Operation, "+", Util.safeConfig.getSingleElement('+'), 11, 11),
                Token(TokenType.Long, "3", null, 12, 12),
                Token(TokenType.Support, ",", null,13, 13),
                Token(TokenType.String, "fred", null,14, 19),
                Token(TokenType.Support, ",", null,20, 20),
                Token(TokenType.Name, "field", null,21, 25),
                Token(TokenType.Support, ")",  null,26, 26)
            )
        }

        test("Function no parameters") {
            val results = Util.tokeniser.tokenise("function()" )
            results shouldBe listOf(
                Token(TokenType.Name, "function",null, 0, 7),
                Token(TokenType.Support, "(", null, 8, 8),
                Token(TokenType.Support, ")", null, 9, 9)
            )
        }

        test("Conditional operator") {
            val results = Util.tokeniser.tokenise("true ? 'test' : 'test2'"  )
            results shouldBe listOf(
                Token(TokenType.Boolean, "true", null, 0, 3),
                Token(TokenType.Operation, "?", Util.safeConfig.getSingleElement('?'), 5, 5),
                Token(TokenType.String, "test", null, 7, 12),
                Token(TokenType.Operation, ":", Util.safeConfig.getSingleElement(':'), 14, 14),
                Token(TokenType.String, "test2", null, 16, 22)
            )
        }

        test("Unknown element") {
            shouldThrow<UnknownElementException> {
                Util.tokeniser.tokenise("}"  )
            }

        }

    }
}
