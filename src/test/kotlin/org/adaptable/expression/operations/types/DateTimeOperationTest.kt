package org.adaptable.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.mockito.Mockito.mock
import java.time.LocalDateTime

class DateTimeOperationTest  : FunSpec() {

    init {
        test("Datetime value") {
            DateTimeOperation(LocalDateTime.of(2010,10,10,10,10,10)).execute(mock(Context::class.java)) shouldBe LocalDateTime.of(2010,10,10,10,10,10)
        }
    }
}
