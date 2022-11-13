package org.adaptable.expression.operations

abstract class BinaryOperation(
    protected val leftOperation: Operation,
    protected val rightOperation: Operation
) : Operation