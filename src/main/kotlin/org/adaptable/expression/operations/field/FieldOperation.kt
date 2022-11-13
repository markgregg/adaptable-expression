package org.adaptable.expression.operations.field

import com.fasterxml.jackson.databind.node.ObjectNode
import org.adaptable.expression.Context
import org.adaptable.expression.exceptions.IncompatibleTypeException
import org.adaptable.expression.operations.Operation
import org.adaptable.expression.operations.UnaryOperation
import org.adaptable.expression.operations.Util.convertToScriptType
import org.adaptable.expression.operations.Util.getJsonValue

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