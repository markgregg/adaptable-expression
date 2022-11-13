package org.adaptable.expression.operations.functions

import org.adaptable.expression.operations.Util.castToType
import java.time.LocalDate
import java.time.LocalDateTime

internal object Functions {
    fun left(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.substring(0, (parameters[1] as Long).toInt())
        } else {
            target.toString().substring(0,  (parameters[1] as Long).toInt())
        }
    }

    fun right(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.substring(target.length -  (parameters[1] as Long).toInt())
        } else {
            target.toString().substring(target.toString().length - (parameters[1] as Long).toInt())
        }
    }

    fun substring(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.substring( (parameters[1] as Long).toInt(), (parameters[2] as Long).toInt())
        } else {
            target.toString().substring( (parameters[1] as Long).toInt(), (parameters[2] as Long).toInt())
        }
    }

    fun lowercase(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.lowercase()
        } else {
            target.toString().lowercase()
        }
    }

    fun uppercase(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.uppercase()
        } else {
            target.toString().uppercase()
        }
    }

    fun trim(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.trim()
        } else {
            target.toString().trim()
        }
    }

    fun trimStart(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.trimStart()
        } else {
            target.toString().trimStart()
        }
    }

    fun trimEnd(parameters: List<Any>): Any? {
        val target = parameters[0]
        return if( target is String ) {
            target.trimEnd()
        } else {
            target.toString().trimEnd()
        }
    }

    fun toString(parameters: List<Any>): Any {
        return castToType(parameters[0], String::class.java)
    }

    fun toLong(parameters: List<Any>): Any {
        return castToType(parameters[0], Long::class.java)
    }

    fun toDouble(parameters: List<Any>): Any {
        return castToType(parameters[0], Double::class.java)
    }

    fun toBoolean(parameters: List<Any>): Any {
        return castToType(parameters[0], Boolean::class.java)
    }

    fun toDate(parameters: List<Any>): Any {
        return castToType(parameters[0], LocalDate::class.java)
    }

    fun toDateTime(parameters: List<Any>): Any {
        return castToType(parameters[0], LocalDateTime::class.java)
    }

    fun getDate(): Any {
        return LocalDate.now()
    }

    fun getDateTime(): Any {
        return LocalDateTime.now()
    }
}