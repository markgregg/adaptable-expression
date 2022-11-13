package org.adaptable.expression.config

import org.adaptable.expression.annotations.Configuration
import org.adaptable.expression.annotations.Elements
import org.adaptable.expression.annotations.Functions
import org.adaptable.expression.operations.functions.FunctionDeclaration
import org.adaptable.expression.operations.functions.ParameterlessFunction

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