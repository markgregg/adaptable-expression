package org.adaptable.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.mockito.Mockito.mock
import java.time.LocalDate

class DateOperationTest  : FunSpec() {

    init {
        test("Date value") {
            DateOperation(LocalDate.of(2010,10,10)).execute(mock(Context::class.java)) shouldBe LocalDate.of(2010,10,10)
        }
    }
}
