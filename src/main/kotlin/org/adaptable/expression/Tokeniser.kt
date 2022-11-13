package org.adaptable.expression

import org.adaptable.expression.config.Configuration
import org.adaptable.expression.exceptions.MissingSymbolException
import org.adaptable.expression.exceptions.UnknownElementException


class Tokeniser internal constructor(val configuration: Configuration){

    companion object {
        private val instance = Tokeniser(Configuration.instance)
        @JvmStatic
        fun instance(): Tokeniser {
            return instance
        }
    }

    fun tokenise(expression: String): List<Token> {
        val tokens = ArrayList<Token>()
        var position = 0
        while( position < expression.length) {
            val ch = expression[position]
            if( ch.isDigit()
                || (ch == '.' && position+1 < expression.length && expression[position+1].isDigit())
                || (ch == '-' && position+1 < expression.length && (expression[position+1].isDigit() || expression[position+1] == '.'))) {
                position = readNumber(tokens, expression, position)
            } else if( ch.isLetter()) {
                position = readName(tokens, expression, position)
            } else if( ch.isWhitespace()) {
                while (position < expression.length && expression[position].isWhitespace()) position++
            } else {
                if( ch == configuration.stringIdentifier ) {
                    position = readString(tokens, expression, position)
                } else if( ch == configuration.dateIdentifier ) {
                    position = readDate(tokens, expression, position)
                } else if( ch == configuration.functionStart
                    || ch == configuration.functionEnd
                    || ch == configuration.functionSeparator
                    || ch == configuration.scopeStart
                    || ch == configuration.scopeEnd ) {
                    tokens.add(Token(
                        TokenType.Support,
                        expression[position].toString(),
                        null,
                        position,
                        position++
                    ))
                } else {
                    if( position + 3 <= expression.length) {
                        val tripleCh = expression.substring(position, position + 3)
                        val element = configuration.getTripleElement(tripleCh)
                        if( element != null ) {
                            tokens.add(
                                Token(
                                    TokenType.Operation,
                                    tripleCh,
                                    element,
                                    position,
                                    position + 2
                                )
                            )
                            position += 3
                            continue
                        }
                    }
                     if ( position + 2 <= expression.length ) {
                         val dualCh = expression.substring(position, position + 2)
                         val element = configuration.getDualElement(dualCh)
                         if( element != null) {
                             tokens.add(
                                 Token(
                                     TokenType.Operation,
                                     dualCh,
                                     element,
                                     position,
                                     position + 1
                                 )
                             )
                             position += 2
                             continue
                         }
                    }
                    val element = configuration.getSingleElement(ch)
                    if( element != null ) {
                        tokens.add(Token(
                            TokenType.Operation,
                            ch.toString(),
                            element,
                            position,
                            position
                        ))
                        position++
                    } else {
                        throw UnknownElementException("$ch at position $position is not recognised")
                    }
                }
            }
        }
        return tokens
    }

    private fun readString(tokens: MutableList<Token>, expression: String, start: Int): Int {
        var position = start+1
        while(position < expression.length
            && expression[position] != '\'' ) position++
        if( position >= expression.length) {
            throw MissingSymbolException("No match quote for opening quote at $start")
        }
        tokens.add(Token(TokenType.String,
            expression.substring(start+1, position),
            null,
            start,
            position)
        )
        position++
        return position
    }

    private fun readDate(tokens: MutableList<Token>, expression: String, start: Int): Int {
        var position = start+1
        while(position < expression.length
            && expression[position] != '#' ) position++
        if( position >= expression.length) {
            throw MissingSymbolException("No match hash for opening hash at $start")
        }
        tokens.add(Token(
            TokenType.DateTime,
            expression.substring(start+1, position),
            null,
            start,
            position
        ))
        position++
        return position
    }

    private fun readNumber(tokens: MutableList<Token>, expression: String, start: Int): Int {
        var position = start+1
        while (position < expression.length
            && ( expression[position].isDigit()
                    || expression[position] == '.') ) position++
        val number = expression.substring(start, position)
        tokens.add(Token(
            if( number.contains('.') ) TokenType.Double else TokenType.Long,
            expression.substring(start, position),
            null,
            start,
            position-1
        ))
        return position
    }

    private fun readName(tokens: MutableList<Token>, expression: String, start: Int): Int {
        var position = start
        while (position < expression.length
            && (expression[position].isLetter()
                    || expression[position].isDigit()
                    || expression[position] == '-')) position++
        val text = expression.substring(start, position)
        tokens.add(Token(
            if( text.lowercase() == "true" || text.lowercase() == "false") TokenType.Boolean else TokenType.Name,
            text.lowercase(),
            null,
            start,
            position-1
        ))
        return position
    }

}