package io.github.markgregg.expression

import io.github.markgregg.expression.config.Configuration
import io.github.markgregg.expression.config.Element
import io.github.markgregg.expression.config.OperandType
import io.github.markgregg.expression.exceptions.*
import io.github.markgregg.expression.operations.Operation
import io.github.markgregg.expression.operations.functions.Function
import io.github.markgregg.expression.operations.functions.FunctionOperation
import io.github.markgregg.expression.operations.types.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

class ExpressionParser internal constructor(val configuration: Configuration){

    companion object {
        private val instance = ExpressionParser(Configuration.instance)
        @JvmStatic
        fun instance(): ExpressionParser {
            return ExpressionParser.Companion.instance
        }
    }

    fun parse(tokens: List<Token>): Operation {
        val position = AtomicInteger(0)
        return getOperation(tokens, position, null, 0)
    }

    private fun getOperation(
        tokens: List<Token>,
        position: AtomicInteger,
        symbol: String?,
        precedence: Int
    ): Operation {
        var currentOp: Operation? = null
        while (position.get() < tokens.size) {
            val token = tokens[position.get()]
            position.incrementAndGet()
            when (token.tokenType) {
                TokenType.String -> {
                    currentOp = StringOperation(token.token)
                }
                TokenType.Double -> {
                    currentOp = DoubleOperation(token.token.toDouble())
                }
                TokenType.Long -> {
                    currentOp = LongOperation(token.token.toLong())
                }
                TokenType.Boolean -> {
                    currentOp = BooleanOperation(token.token.toBoolean())
                }
                TokenType.DateTime -> {
                    currentOp = getDateTimeOp(token)
                }
                TokenType.Name -> {
                    currentOp = getNameOp(tokens, token, position)
                }
                TokenType.Operation -> {
                    if( precedence > (token.element?.precedence ?: 99)) {
                        position.decrementAndGet()
                        break
                    }
                    when (token.element?.operators) {
                        OperandType.Unary -> {
                            currentOp = getUnaryOp(tokens, token.element, position, currentOp, symbol)
                        }
                        OperandType.Binary -> {
                            if( currentOp == null ) {
                                throw ParseException("Binary operator ${token.token} is missing left operand at ${token.start}")
                            }
                            currentOp = getBinaryOp(tokens, token.element, position, currentOp, symbol)
                        }
                        OperandType.Ternary -> {
                            if( currentOp == null ) {
                                throw ParseException("Ternary operator ${token.token} is missing left operand at ${token.start}")
                            }
                            currentOp = getTernaryOp(tokens, token.element, position, currentOp, symbol)
                        }
                        else -> {
                            currentOp = getNonOp(tokens, token.element!!, position)
                        }
                    }
                }
                TokenType.Support -> {
                    if(token.token != configuration.scopeStart.toString() &&
                        token.token != configuration.scopeEnd.toString()) {
                        throw InvalidTokenException("Invalid ${token.token} at ${token.start}")
                    }
                    if(token.token == configuration.scopeStart.toString() ) {
                        currentOp = getOperation(tokens, position, configuration.scopeEnd.toString(), 0)
                    }
                }
            }
            if (position.get() < tokens.size &&
                tokens[position.get()].token == symbol) {
                break
            }
        }
        if( currentOp == null) {
            throw ParseException("Expressions must return a value")
        }
        return currentOp
    }

    private fun getDateTimeOp(token: Token): Operation {
        val dateTime =getDateTime(token.token)
        return if( dateTime is LocalDate ) {
            DateOperation(dateTime)
        } else {
            DateTimeOperation(dateTime as LocalDateTime)
        }
    }

    private fun getDateTime(date: String): Any {
        return try {
            if (date.length == 10) {
                LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
            } else {
                LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }
        } catch (e: Exception) {
            throw InvalidDateException("$date is not a recognised format, please use ISO")
        }
    }

    private fun getNameOp(tokens: List<Token>, token: Token, position: AtomicInteger): Operation {
        return if( position.get() < tokens.size && tokens[position.get()].token == configuration.functionStart.toString() ) {
            val function = configuration.getFunction(token.token) ?: throw UnknownFunctionException("Function ${token.token} cannot be found")
            position.incrementAndGet()
            val parameters = if( function is Function) {
                (1..function.parameters.size)
                    .map {
                        val op = getOperation(
                            tokens, position,
                            if (it == function.parameters.size) ")" else ",",
                            99
                        )
                        val separatorToken = if( position.get() < tokens.size ) {
                            tokens[position.get()]
                        } else {
                            tokens[position.get()-1]
                        }
                        if ( it < function.parameters.size && separatorToken.token != "," ||
                            it == function.parameters.size && separatorToken.token != ")") {
                            throw MissingSymbolException("Function arguments must be separated with a , and end with a ) at ${separatorToken.start}")
                        }
                        position.incrementAndGet()
                        op
                    }
            } else {
                emptyList()
            }
            position.incrementAndGet()
            FunctionOperation(function, parameters)
        } else {
            throw MissingSymbolException("Expecting a ( to follow function ${token.token} at ${token.start}")
        }
    }

    private fun getUnaryOp(tokens: List<Token>, element: Element, position: AtomicInteger, leftOp: Operation?, symbol: String?): Operation {
        return if( element.nameFollows ) {
            val token = tokens[position.getAndIncrement()]
            if( token.tokenType != TokenType.Name) {
                throw MissingSymbolException("Expecting name following ${element.token1} at ${token.start}")
            }
            val op = getOperation(tokens, element, position, leftOp, symbol)
            element.operation.getDeclaredConstructor(String::class.java, Operation::class.java).newInstance(token.token, op) as Operation
        } else {
            val op = getOperation(tokens, element, position, leftOp, symbol)
            element.operation.getDeclaredConstructor(Operation::class.java).newInstance(op) as Operation
        }
    }

    private fun getOperation(tokens: List<Token>, element: Element, position: AtomicInteger, leftOp: Operation?, symbol: String?): Operation {
        return if( element.applyBefore ) {
            if( leftOp == null ) {
                throw ParseException("Unary operator ${element.token1} must be applied to something")
            }
            leftOp
        } else {
            getOperation(tokens, position, symbol, element.precedence)
        }
    }

    private fun getBinaryOp(tokens: List<Token>, element: Element, position: AtomicInteger, leftOp: Operation, symbol: String?): Operation {
        val rightOp = getOperation(tokens, position, element.token2 ?: symbol, element.precedence)
        if( element.token2 != null ) {
            if( tokens[position.get()].token != element.token2) {
                throw MissingSymbolException("Expecting name following ${element.token2} at ${tokens[position.get()].start}")
            }
            position.incrementAndGet()
        }
        return element.operation.getDeclaredConstructor(Operation::class.java, Operation::class.java)
            .newInstance(leftOp, rightOp) as Operation
    }

    private fun getTernaryOp(tokens: List<Token>, element: Element, position: AtomicInteger, leftOp: Operation, symbol: String?): Operation {
        val centerOp = getOperation(tokens, position, element.token2 ?: symbol, element.precedence)
        if( tokens[position.get()].token != element.token2) {
            throw MissingSymbolException("Expecting name following ${element.token2} at ${tokens[position.get()]}")
        }
        position.incrementAndGet()
        val rightOp = getOperation(tokens, position, symbol, element.precedence)
        return element.operation.getDeclaredConstructor(Operation::class.java, Operation::class.java, Operation::class.java)
            .newInstance(leftOp, centerOp, rightOp) as Operation
    }

    private fun getNonOp(tokens: List<Token>, element: Element, position: AtomicInteger): Operation {
        val token = tokens[position.getAndIncrement()]
        if( token.tokenType != TokenType.Name) {
            throw MissingSymbolException("Expecting name following ${element.token1} at ${token.start}")
        }
        return element.operation.getDeclaredConstructor(String::class.java).newInstance(token.token) as Operation
    }

}
