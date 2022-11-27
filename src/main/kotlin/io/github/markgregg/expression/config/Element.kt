package io.github.markgregg.expression.config

data class Element(
    val token1: String,
    val token2: String?,
    val operation: Class<*>,
    val operators: OperandType,
    val precedence: Int,
    val applyBefore: Boolean = false,
    val nameFollows: Boolean = false
)