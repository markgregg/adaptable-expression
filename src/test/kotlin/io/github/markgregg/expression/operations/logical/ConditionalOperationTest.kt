package io.github.markgregg.expression.operations.logical

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.operations.types.BooleanOperation
import io.github.markgregg.expression.operations.types.StringOperation
import org.mockito.Mockito

class ConditionalOperationTest  : FunSpec() {

    init {
        test("evaluates to true") {
            ConditionalOperation(BooleanOperation(true),StringOperation("left"), StringOperation("right"))
                .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java)) shouldBe "left"
        }

        test("evaluates to false") {
            ConditionalOperation(BooleanOperation(false),StringOperation("left"), StringOperation("right"))
                .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java)) shouldBe "right"
        }
    }
}
