package org.adaptable.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.mockito.Mockito.mock

class DoubleOperationTest  : FunSpec() {

    init {
        test("Boolean value") {
            DoubleOperation(10.10).execute(mock(Context::class.java)) shouldBe 10.10
        }
    }
}
