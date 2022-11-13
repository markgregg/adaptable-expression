package org.adaptable.expression.operations.logical

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.operations.types.BooleanOperation
import org.adaptable.expression.operations.types.StringOperation
import org.mockito.Mockito

class ConditionalOperationTest  : FunSpec() {

    init {
        test("evaluates to true") {
            ConditionalOperation(BooleanOperation(true),StringOperation("left"), StringOperation("right"))
                .execute(Mockito.mock(Context::class.java)) shouldBe "left"
        }

        test("evaluates to false") {
            ConditionalOperation(BooleanOperation(false),StringOperation("left"), StringOperation("right"))
                .execute(Mockito.mock(Context::class.java)) shouldBe "right"
        }
    }
}
