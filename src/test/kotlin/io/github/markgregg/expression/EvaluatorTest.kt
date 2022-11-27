package io.github.markgregg.expression

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.mock

class EvaluatorTest  : FunSpec() {
    init {
        test("evaluate") {
            Util.evaluator.evaluate("12 + 45", mock(Context::class.java)) shouldBe 57L
        }

        test("compile") {
            Util.evaluator.compile("12 + 45").execute(mock(Context::class.java)) shouldBe 57
        }

        test("execute") {
            val operation = Util.evaluator.compile("'the word is' + ' yes'")
            Util.evaluator.execute(operation, mock(Context::class.java)) shouldBe "the word is yes"

        }
    }
}

