package io.github.markgregg.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import org.mockito.Mockito.mock

class StringOperationTest  : FunSpec() {

    init {
        test("Boolean value") {
            StringOperation("test").execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe "test"
        }
    }
}
