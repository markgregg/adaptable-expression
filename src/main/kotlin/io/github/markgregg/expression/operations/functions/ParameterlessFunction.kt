package io.github.markgregg.expression.operations.functions

data class ParameterlessFunction(
    override val name: String,
    val method: () -> Any?
) : FunctionDeclaration