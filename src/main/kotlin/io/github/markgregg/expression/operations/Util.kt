package io.github.markgregg.expression.operations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.markgregg.expression.exceptions.CastException
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Util {
    fun getJsonValue(node: JsonNode): Any {
        return if( node is ObjectNode || node is ArrayNode) {
            node
        } else if( node.isLong || node.isInt || node.isShort ) {
            node.longValue()
        } else if( node.isBoolean ) {
            node.booleanValue()
        } else if( node.isDouble || node.isFloat ) {
            node.doubleValue()
        } else {
            node.textValue()
        }
    }

    fun convertToScriptType(value: Any): Any{
        return when(value) {
            is Int -> value.toLong()
            is Short -> value.toLong()
            is Byte -> value.toLong()
            is Char -> value.toString()
            is Float -> value.toDouble()
            is Date -> value.toLocalDate()
            is Timestamp -> value.toLocalDateTime()
            else -> value
        }
    }

    fun castToType(value: Any, class2: Class<*>): Any {
        return when(class2) {
            String::class.java -> value.toString()
            Boolean::class.java -> {
                when(value) {
                    is String -> value.toBoolean()
                    is Long -> value != 0L
                    is Double -> value != 0.0
                    is Boolean -> value
                    else -> throw CastException("$value cannot be cast to boolean")
                }
            }
            Long::class.java -> {
                when(value) {
                    is String -> value.toLong()
                    is Long -> value
                    is Double -> value.toLong()
                    is Boolean -> if( value ) 1 else 0
                    else -> throw CastException("$value cannot be cast to long")
                }
            }
            Double::class.java -> {
                when(value) {
                    is String -> value.toDouble()
                    is Long -> value.toDouble()
                    is Double -> value
                    is Boolean -> if( value ) 1.0 else 0.0
                    else -> throw CastException("$value cannot be cast to double")
                }
            }
            LocalDate::class.java -> {
                when(value) {
                    is String -> LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
                    is LocalDate -> value
                    is LocalDateTime -> value.toLocalDate()
                    else -> throw CastException("$value cannot be cast to date")
                }
            }
            LocalDateTime::class.java -> {
                when(value) {
                    is String -> LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    is LocalDate -> value.atStartOfDay()
                    is LocalDateTime -> value
                    else -> throw CastException("$value cannot be cast to datetime")
                }
            }
            else -> {
                throw CastException("${class2.name} not handled")
            }
        }
    }
}
