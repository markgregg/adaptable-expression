package org.adaptable.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.mockito.Mockito.mock

class LongOperationTest  : FunSpec() {

    init {
        test("Boolean value") {
            LongOperation(1000).execute(mock(Context::class.java)) shouldBe 1000
        }
    }
}
