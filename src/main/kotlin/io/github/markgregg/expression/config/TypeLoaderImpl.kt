package io.github.markgregg.expression.config

import io.github.classgraph.ClassGraph
import io.github.markgregg.expression.annotations.Configuration

class TypeLoaderImpl(private val filter: String? = null) : TypeLoader {

    private var extensions: List<Class<*>>? = null

    override fun override(): Boolean =
        getExtensions().firstOrNull { !it.getAnnotation(Configuration::class.java).extend } != null

    override fun getExtensions(): List<Class<*>> {
        if( extensions == null ) {
            extensions = ClassGraph()
                .enableClassInfo()
                .enableExternalClasses()
                .ignoreClassVisibility()
                .enableAnnotationInfo()
                .scan().use { result ->
                    val configurations = result.getClassesWithAnnotation(Configuration::class.java).filter {
                        filter == null || it.name.contains(filter)
                    }
                    if (configurations.any { it.getAnnotationInfo(Configuration::class.java).parameterValues.getValue("extend") == false }) {
                        listOf(configurations.first {
                            it.getAnnotationInfo(Configuration::class.java).parameterValues.getValue(
                                "extend"
                            ) == false
                        }.loadClass())
                    } else {
                        configurations.filter {
                            it.getAnnotationInfo(Configuration::class.java).parameterValues.getValue(
                                "extend"
                            ) == true
                        }.map {
                            it.loadClass()
                        }
                    }
                }
        }
        return extensions!!
    }

}
