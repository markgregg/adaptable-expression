package org.adaptable.expression.operations.functions

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.Util
import org.adaptable.expression.exceptions.FunctionException
import org.adaptable.expression.operations.types.LongOperation
import org.adaptable.expression.operations.types.StringOperation
import org.mockito.Mockito.mock
import java.time.LocalDate

class FunctionOperationTest : FunSpec() {

    init {
        test("A called function returns a value") {
            FunctionOperation(Util.safeConfig.getFunction("substr")!!, listOf(StringOperation("this is a string"), LongOperation(2L), LongOperation(4L)))
                .execute(mock(Context::class.java)) shouldBe "is"
        }

        test("A called parameterless function returns a value") {
            FunctionOperation(Util.safeConfig.getFunction("today")!!, emptyList())
                .execute(mock(Context::class.java)) shouldBe LocalDate.now()
        }

        test("Whe a function doesn't return a value it throws an exception") {
            shouldThrow<FunctionException> {
                FunctionOperation(ParameterlessFunction("test") { null }, emptyList())
                    .execute(mock(Context::class.java))
            }
        }
    }
}
