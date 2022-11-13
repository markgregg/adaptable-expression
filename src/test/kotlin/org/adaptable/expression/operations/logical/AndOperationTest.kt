package org.adaptable.expression.operations.logical

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.operations.types.BooleanOperation
import org.mockito.Mockito.mock

class AndOperationTest  : FunSpec() {

    init {
        test("And positive") {
            AndOperation(BooleanOperation(true), BooleanOperation(true)).execute(mock(Context::class.java)) shouldBe true
        }

        test("And negative") {
            AndOperation(BooleanOperation(true), BooleanOperation(false)).execute(mock(Context::class.java)) shouldBe false
        }
    }
}
