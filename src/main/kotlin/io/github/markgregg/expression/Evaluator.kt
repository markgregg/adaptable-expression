package io.github.markgregg.expression

import io.github.markgregg.expression.config.Configuration
import io.github.markgregg.expression.operations.Operation


class Evaluator internal constructor(val configuration: Configuration){

    companion object {
        private val instance = Evaluator(Configuration.instance)
        @JvmStatic
        fun instance(): Evaluator {
            return Evaluator.Companion.instance
        }
    }

    private val tokeniser = Tokeniser(configuration)
    private val parser = ExpressionParser(configuration)

    fun evaluate(expression: String, context: Context): Any {
        return parser.parse(tokeniser.tokenise(expression)).execute(context)
    }

    fun compile(expression: String): Operation {
        return parser.parse(tokeniser.tokenise(expression))
    }

    fun execute(operation: Operation, context: Context): Any {
        return operation.execute(context)
    }
}