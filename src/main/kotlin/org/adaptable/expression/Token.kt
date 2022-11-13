package org.adaptable.expression

import org.adaptable.expression.config.Element


data class Token(
    val tokenType: TokenType,
    val token: String,
    val element: Element?,
    val start: Int,
    val end: Int
)
