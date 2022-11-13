package org.adaptable.expression.operations.field

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

class ContextOperationTest : FunSpec() {

    init {
        test("execute") {

            val context = mock(Context::class.java)
            whenever(context.getItem(eq("field"))).thenReturn("value")
            ContextOperation("field").execute(context) shouldBe "value"
        }
    }
}
