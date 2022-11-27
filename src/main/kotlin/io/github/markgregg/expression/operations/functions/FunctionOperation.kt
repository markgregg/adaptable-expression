package io.github.markgregg.expression.operations.functions

import io.github.markgregg.expression.Context
import io.github.markgregg.expression.exceptions.FunctionException
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.Util.castToType

class FunctionOperation(
    private val function: FunctionDeclaration,
    private val parameterOps: List<Operation>
) : Operation {
    override fun execute(context: Context): Any {
        return if( function is Function ) {
            val params = parameterOps.mapIndexed { index,op ->
                castToType(op.execute(context), function.parameters[index])
            }
            function.method.invoke(params)
        } else {
            (function as ParameterlessFunction).method.invoke()
        } ?: throw FunctionException("${function.name} did not return a value")
    }
}