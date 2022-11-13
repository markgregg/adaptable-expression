package org.adaptable.expression.operations.field

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.adaptable.expression.Context
import org.adaptable.expression.exceptions.ArrayException
import org.adaptable.expression.exceptions.IncompatibleTypeException
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.relational.EqualsOperation
import org.adaptable.expression.operations.types.LongOperation
import org.adaptable.expression.operations.types.StringOperation
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class ArrayOperationTest : FunSpec() {
    init {
        test("Context not an array throws an exception") {
            shouldThrow<IncompatibleTypeException> {
                ArrayOperation( LongOperation(1), LongOperation(1)).execute(mock(Context::class.java))
            }
        }

        test("Returns element at index") {
            val json = jacksonObjectMapper().writeValueAsString(listOf(10,20,30,40))
            val op = mock(Operation::class.java)
            whenever(op.execute(any())).thenReturn(jacksonObjectMapper().readTree(json))
            ArrayOperation(op, LongOperation(2)).execute(mock(Context::class.java)) shouldBe 30
        }


        test("Returns element in map") {
            val op = mock(Operation::class.java)
            whenever(op.execute(any())).thenReturn(jacksonObjectMapper().readTree("{\"field1\":\"test\"}"))
            ArrayOperation(op, StringOperation("field1")).execute(mock(Context::class.java)) shouldBe "test"
        }

        test("Returns element at matching element") {

            val json = jacksonObjectMapper().writeValueAsString(listOf(Test("fred"), Test("test")))
            val op = mock(Operation::class.java)
            whenever(op.execute(any())).thenReturn(jacksonObjectMapper().readTree(json))
            ArrayOperation(op, EqualsOperation(ContextOperation("field1"),StringOperation("test")))
                .execute(mock(Context::class.java)) shouldBe jacksonObjectMapper().readTree("{\"field1\":\"test\"}")
        }

        test("Element not found") {

            shouldThrow<ArrayException> {
                val json = jacksonObjectMapper().writeValueAsString(listOf(Test("fred"), Test("test")))
                val op = mock(Operation::class.java)
                whenever(op.execute(any())).thenReturn(jacksonObjectMapper().readTree(json))
                ArrayOperation(op, EqualsOperation(ContextOperation("field1"),StringOperation("xxx")))
                    .execute(mock(Context::class.java)) shouldBe jacksonObjectMapper().readTree("{\"field1\":\"test\"}")
            }

        }
    }

    private data class Test(
        val field1: String
    )
}
