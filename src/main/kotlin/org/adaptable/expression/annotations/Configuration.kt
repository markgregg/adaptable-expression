package org.adaptable.expression.annotations

@Target(AnnotationTarget.CLASS)
annotation class Configuration(
    val extend: Boolean = true,
    val functionStart: Char = Char.MIN_VALUE,
    val functionEnd: Char = Char.MIN_VALUE,
    val functionSeparator: Char = Char.MIN_VALUE,
    val scopeStart: Char = Char.MIN_VALUE,
    val scopeEnd: Char = Char.MIN_VALUE,
    val stringIdentifier: Char = Char.MIN_VALUE,
    val dateIdentifier: Char = Char.MIN_VALUE
)
