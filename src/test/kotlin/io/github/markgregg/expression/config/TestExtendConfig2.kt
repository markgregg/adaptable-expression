package io.github.markgregg.expression.config

import io.github.markgregg.expression.annotations.Configuration
import io.github.markgregg.expression.annotations.Elements
import io.github.markgregg.expression.annotations.Functions
import io.github.markgregg.expression.operations.functions.FunctionDeclaration

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