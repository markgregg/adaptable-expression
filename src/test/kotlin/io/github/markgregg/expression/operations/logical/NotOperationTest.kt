package io.github.markgregg.expression.operations.logical

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.types.BooleanOperation
import org.mockito.Mockito.mock

class NotOperationTest  : FunSpec() {

    init {
        test("Not positive") {
            NotOperation(BooleanOperation(true)).execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe false
        }

        test("Not negative") {
            NotOperation(BooleanOperation(false)).execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe true
        }
    }
}
