package io.github.markgregg.expression.operations.functions

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.Util
import io.github.markgregg.expression.exceptions.FunctionException
import io.github.markgregg.expression.operations.types.LongOperation
import io.github.markgregg.expression.operations.types.StringOperation
import org.mockito.Mockito.mock
import java.time.LocalDate

class FunctionOperationTest : FunSpec() {

    init {
        test("A called function returns a value") {
            FunctionOperation(Util.safeConfig.getFunction("substr")!!, listOf(StringOperation("this is a string"), LongOperation(2L), LongOperation(4L)))
                .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe "is"
        }

        test("A called parameterless function returns a value") {
            FunctionOperation(Util.safeConfig.getFunction("today")!!, emptyList())
                .execute(mock(io.github.markgregg.expression.Context::class.java)) shouldBe LocalDate.now()
        }

        test("Whe a function doesn't return a value it throws an exception") {
            shouldThrow<FunctionException> {
                FunctionOperation(ParameterlessFunction("test") { null }, emptyList())
                    .execute(mock(io.github.markgregg.expression.Context::class.java))
            }
        }
    }
}
