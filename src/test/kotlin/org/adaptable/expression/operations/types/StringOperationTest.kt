package org.adaptable.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.mockito.Mockito.mock

class StringOperationTest  : FunSpec() {

    init {
        test("Boolean value") {
            StringOperation("test").execute(mock(Context::class.java)) shouldBe "test"
        }
    }
}
