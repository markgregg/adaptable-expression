package org.adaptable.expression.config

import org.adaptable.expression.annotations.Configuration
import org.adaptable.expression.annotations.Elements
import org.adaptable.expression.annotations.Functions
import org.adaptable.expression.operations.functions.FunctionDeclaration

@Configuration( extend = true )
class TestExtendConfig2 {

    @Elements
    fun elements(): List<Element> {
        return emptyList()
    }

    @Functions
    fun functions(): List<FunctionDeclaration> {
        return emptyList()
    }

    private fun example(): Any {
        return ""
    }
}