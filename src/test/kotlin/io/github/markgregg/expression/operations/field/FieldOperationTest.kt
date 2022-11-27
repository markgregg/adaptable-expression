package io.github.markgregg.expression.operations.field

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.Operation
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class FieldOperationTest : FunSpec() {

    init {
        test("When context not a json object throw exception") {
            shouldThrow<IncompatibleTypeException> {
                val op = Mockito.mock(Operation::class.java)
                whenever(op.execute(any())).thenReturn("")

                FieldOperation("field", op).execute(Mockito.mock(io.github.markgregg.expression.Context::class.java))
            }
        }

        context("test fetch fields") {
            listOf(
                Pair("field1", "test"),
                Pair("field2", 100L),
                Pair("field3", 200L),
                Pair("field4", 400L),
                Pair("field5", true),
                Pair("field6", 10.10),
                Pair("field7", 100.100)
            ).forEach {

                test("Correct value is returned for ${it.first}") {
                    val json = jacksonObjectMapper().writeValueAsString(
                        Test("test", 100, 200, 400, true, 10.10F, 100.100)
                    )
                    val op = Mockito.mock(Operation::class.java)
                    whenever(op.execute(any())).thenReturn(jacksonObjectMapper().readTree(json))
                    FieldOperation(it.first, op)
                        .execute(Mockito.mock(io.github.markgregg.expression.Context::class.java)) shouldBe it.second
                }
            }

        }
    }

    private data class Test(
        val field1: String,
        val field2: Int,
        val field3: Short,
        val field4: Long,
        val field5: Boolean,
        val field6: Float,
        val field7: Double
    )
}
