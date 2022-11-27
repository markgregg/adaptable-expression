package io.github.markgregg.expression

import io.github.markgregg.expression.config.Element


data class Token(
    val tokenType: TokenType,
    val token: String,
    val element: Element?,
    val start: Int,
    val end: Int
)
