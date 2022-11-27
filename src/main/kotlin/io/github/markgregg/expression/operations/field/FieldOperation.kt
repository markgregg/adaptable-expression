package io.github.markgregg.expression.operations.field

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.IncompatibleTypeException
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.UnaryOperation
import io.github.markgregg.expression.operations.Util.convertToScriptType
import io.github.markgregg.expression.operations.Util.getJsonValue

class FieldOperation(
    private val field: String,
    operation: Operation
) : UnaryOperation(operation) {
    override fun execute(context: Context): Any {
        val target = operation.execute(context)
        if( target !is ObjectNode) {
            throw IncompatibleTypeException("Value is not a json object")
        }
        return convertToScriptType(getJsonValue(target.get(field)))
    }

}