package org.adaptable.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.mockito.Mockito.mock

class BooleanOperationTest  : FunSpec() {

    init {
        test("Boolean value") {
            BooleanOperation(true).execute(mock(Context::class.java)) shouldBe true
        }
    }
}

