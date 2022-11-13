package org.adaptable.expression.operations.logical

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.operations.types.BooleanOperation
import org.mockito.Mockito.mock

class NotOperationTest  : FunSpec() {

    init {
        test("Not positive") {
            NotOperation(BooleanOperation(true)).execute(mock(Context::class.java)) shouldBe false
        }

        test("Not negative") {
            NotOperation(BooleanOperation(false)).execute(mock(Context::class.java)) shouldBe true
        }
    }
}
