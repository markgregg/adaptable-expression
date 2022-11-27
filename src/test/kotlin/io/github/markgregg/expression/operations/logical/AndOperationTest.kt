package io.github.markgregg.expression.operations.logical

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.types.BooleanOperation
import org.mockito.Mockito.mock

class AndOperationTest  : FunSpec() {

    init {
        test("And positive") {
            AndOperation(BooleanOperation(true), BooleanOperation(true)).execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
        }

        test("And negative") {
            AndOperation(BooleanOperation(true), BooleanOperation(false)).execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
        }
    }
}
