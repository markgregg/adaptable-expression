package io.github.markgregg.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import org.mockito.Mockito.mock

class LongOperationTest  : FunSpec() {

    init {
        test("Boolean value") {
            LongOperation(1000).execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe 1000
        }
    }
}
