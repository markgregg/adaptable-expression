package io.github.markgregg.expression.config

import io.github.markgregg.expression.annotations.Configuration
import io.github.markgregg.expression.annotations.Elements
import io.github.markgregg.expression.annotations.Functions
import io.github.markgregg.expression.operations.functions.FunctionDeclaration
import io.github.markgregg.expression.operations.functions.ParameterlessFunction

@Configuration(
    extend = false,
    functionStart = '*',
    functionEnd = '&',
    functionSeparator = '~',
    scopeStart = '[',
    scopeEnd = ']',
    stringIdentifier = '"',
    dateIdentifier = '%'
)
class TestConfig {

    @Elements
    fun elements(): List<Element> {
        return listOf(
            Element("---", null, TestOp::class.java, OperandType.Unary, 1,true )
        )
    }

    @Functions
    fun functions(): List<FunctionDeclaration> {
        return listOf(
            ParameterlessFunction("test", this::example)
        )
    }

    private fun example(): Any {
        return ""
    }
}