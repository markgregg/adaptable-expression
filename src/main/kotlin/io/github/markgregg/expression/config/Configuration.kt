package io.github.markgregg.expression.config

import io.github.markgregg.expression.annotations.Configuration
import io.github.markgregg.expression.annotations.Elements
import io.github.markgregg.expression.operations.arithematic.*
import io.github.markgregg.expression.operations.field.ArrayOperation
import io.github.markgregg.expression.operations.field.ContextOperation
import io.github.markgregg.expression.operations.field.FieldOperation
import io.github.markgregg.expression.operations.functions.Function
import io.github.markgregg.expression.operations.functions.FunctionDeclaration
import io.github.markgregg.expression.operations.functions.Functions
import io.github.markgregg.expression.operations.functions.ParameterlessFunction
import io.github.markgregg.expression.operations.logical.AndOperation
import io.github.markgregg.expression.operations.logical.ConditionalOperation
import io.github.markgregg.expression.operations.logical.NotOperation
import io.github.markgregg.expression.operations.logical.OrOperation
import io.github.markgregg.expression.operations.relational.*

class Configuration internal constructor(typeLoader: TypeLoader){
    companion object {
        internal val instance = Configuration(TypeLoaderImpl())
    }
    private val elements: List<Element>
    private val singleTokenElements: Map<Char,Element>
    private val dualTokenElements: Map<String,Element>
    private val tripleTokenElements: Map<String,Element>
    private val functions: Map<String, FunctionDeclaration>
    private var configAnnotation: Configuration? = null

    val functionStart: Char = getOverride(typeLoader, Configuration::functionStart) ?: '('
    val functionEnd: Char = getOverride(typeLoader, Configuration::functionEnd) ?: ')'
    val functionSeparator: Char = getOverride(typeLoader,Configuration::functionSeparator) ?: ','
    val scopeStart: Char = getOverride(typeLoader,Configuration::scopeStart) ?: '('
    val scopeEnd: Char = getOverride(typeLoader,Configuration::scopeEnd) ?: ')'
    val stringIdentifier: Char = getOverride(typeLoader,Configuration::stringIdentifier) ?: '\''
    val dateIdentifier: Char = getOverride(typeLoader,Configuration::dateIdentifier) ?: '#'

    init {
        elements = if( typeLoader.override() ) {
            loadExtensionElements(typeLoader)
        } else {
            getInitialElements() + loadExtensionElements(typeLoader)
        }
        functions= if( typeLoader.override() ) {
            loadExtensionFunctions(typeLoader)
        } else {
            getInitialFunctions() + loadExtensionFunctions(typeLoader)
        }
        singleTokenElements = elements.filter { it.token1.length == 1 }.associateBy { it.token1[0] } + elements.filter{ it.token2?.length == 1 }.associateBy { it.token2?.get(0)!! }
        dualTokenElements = elements.filter { it.token1.length == 2 }.associateBy { it.token1 } +  elements.filter { it.token2?.length == 2 }.associateBy { it.token2!! }
        tripleTokenElements = elements.filter { it.token1.length == 3 }.associateBy { it.token1 } + elements.filter { it.token2?.length == 3 }.associateBy { it.token2!! }
    }

    fun getSingleElement(token: Char): Element? {
        return singleTokenElements[token]
    }

    fun getDualElement(token: String): Element? {
        return dualTokenElements[token]
    }
    fun getTripleElement(token: String): Element? {
        return tripleTokenElements[token]
    }

    fun getFunction(name: String): FunctionDeclaration? {
        return functions[name]
    }

    private fun getInitialElements(): List<Element> {
        return listOf(
            Element("[", "]",ArrayOperation::class.java, OperandType.Binary, 0 ),
            Element( ".", null, FieldOperation::class.java, OperandType.Unary, 0, applyBefore = true, nameFollows = true),
            Element( "$", null, ContextOperation::class.java, OperandType.NotOperand, 0),
            Element( "?", ":", ConditionalOperation::class.java, OperandType.Ternary, 0),
            Element( "&&", null,AndOperation::class.java, OperandType.Binary, 1),
            Element( "||", null,OrOperation::class.java, OperandType.Binary, 1),
            Element( "==", null,EqualsOperation::class.java, OperandType.Binary, 2),
            Element( "!=", null,NotEqualsOperation::class.java, OperandType.Binary, 2),
            Element( ">", null,GreaterOperation::class.java, OperandType.Binary, 2),
            Element( "<", null,LessOperation::class.java, OperandType.Binary, 2),
            Element( ">=", null,GreaterEqualsOperation::class.java, OperandType.Binary, 2),
            Element( "<=", null,LessEqualsOperation::class.java, OperandType.Binary, 2),
            Element( "+", null,AddOperation::class.java, OperandType.Binary, 3),
            Element( "-", null,MinusOperation::class.java, OperandType.Binary, 3),
            Element( "*", null,MultiplyOperation::class.java, OperandType.Binary, 4),
            Element( "/", null,DivideOperation::class.java, OperandType.Binary, 4),
            Element( "%", null,ModOperation::class.java, OperandType.Binary, 4),
            Element( "!", null,NotOperation::class.java, OperandType.Unary, 999)
        )
    }

    private fun getInitialFunctions(): Map<String, FunctionDeclaration> {
        val functions = listOf(
            Function("left", Functions::left, listOf(String::class.java,Long::class.java)),
            Function("right", Functions::right, listOf(String::class.java,Long::class.java)),
            Function("substr", Functions::substring, listOf(String::class.java,Long::class.java, Long::class.java)),
            Function("lcase", Functions::lowercase, listOf(String::class.java)),
            Function("ucase", Functions::uppercase, listOf(String::class.java)),
            Function("trim", Functions::trim, listOf(String::class.java)),
            Function("trimstart", Functions::trimStart, listOf(String::class.java)),
            Function("trimend", Functions::trimEnd, listOf(String::class.java)),
            Function("tostr", Functions::toString, listOf(Any::class.java)),
            Function("tolng", Functions::toLong, listOf(Any::class.java)),
            Function("todbl", Functions::toDouble, listOf(Any::class.java)),
            Function("tobool", Functions::toBoolean, listOf(Any::class.java)),
            Function("todate", Functions::toDate, listOf(Any::class.java)),
            Function("todatetime", Functions::toDateTime, listOf(Any::class.java)),
            ParameterlessFunction("today", Functions::getDate),
            ParameterlessFunction("now", Functions::getDateTime),
        )

        return functions.associateBy { it.name }
    }

    private fun loadExtensionElements(typeLoader: TypeLoader): List<Element> {
        return typeLoader.getExtensions().map { clazz ->
            clazz.declaredMethods.filter { method ->
                method.getAnnotation(Elements::class.java) != null
            }.map { method ->
                Pair(clazz.getConstructor().newInstance(), method)
            }
        }.flatMap {
            it.flatMap { p ->
                List::class.java.cast(p.second.invoke(p.first))
                    .filterIsInstance(Element::class.java)
            }
        }
    }

    private fun loadExtensionFunctions(typeLoader: TypeLoader): Map<String, FunctionDeclaration> {
        return typeLoader.getExtensions().map { clazz ->
            clazz.declaredMethods.filter { method ->
                method.getAnnotation(io.github.markgregg.expression.annotations.Functions::class.java) != null
            }.map { method ->
                Pair(clazz.getConstructor().newInstance(), method)
            }
        }.flatMap {
            it.flatMap { p ->
                List::class.java.cast(p.second.invoke(p.first))
                    .filterIsInstance(FunctionDeclaration::class.java)
            }
        }.associateBy{ it.name }
    }

    private fun getOverride(typeLoader: TypeLoader, getter: (Configuration) -> Char ): Char? {
        if( !typeLoader.override()) {
            return null
        }
        if( configAnnotation == null ) {
            configAnnotation = typeLoader.getExtensions().firstOrNull()
                ?.getAnnotation(Configuration::class.java)
        }
        return if( configAnnotation != null) {
            val value = getter(configAnnotation!!)
            return if (value == Char.MIN_VALUE) {
                null
            } else {
                value
            }
        } else {
            null
        }
    }
}