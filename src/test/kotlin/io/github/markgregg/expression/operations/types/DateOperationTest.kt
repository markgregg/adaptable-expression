package io.github.markgregg.expression.operations.types

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import org.mockito.Mockito.mock
import java.time.LocalDate

class DateOperationTest  : FunSpec() {

    init {
        test("Date value") {
            DateOperation(LocalDate.of(2010,10,10)).execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe LocalDate.of(2010,10,10)
        }
    }
}
