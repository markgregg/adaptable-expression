package io.github.markgregg.expression.operations.functions

data class Function(
    override val name: String,
    val method: (parameters: List<Any>) -> Any?,
    val parameters: List<Class<*>>
) : FunctionDeclaration
