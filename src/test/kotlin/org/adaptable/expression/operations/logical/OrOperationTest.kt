package org.adaptable.expression.operations.logical

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.operations.types.BooleanOperation
import org.mockito.Mockito.mock

class OrOperationTest  : FunSpec() {

    init {
        test("Or positive") {
            OrOperation(BooleanOperation(false), BooleanOperation(true)).execute(mock(Context::class.java)) shouldBe true
        }

        test("Or negative") {
            OrOperation(BooleanOperation(false), BooleanOperation(false)).execute(mock(Context::class.java)) shouldBe false
        }
    }
}
