package org.adaptable.expression.operations

abstract class TernaryOperation(
    protected val leftOperation: Operation,
    protected val centerOperation: Operation,
    protected val rightOperation: Operation
) : Operation