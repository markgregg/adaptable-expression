package io.github.markgregg.expression

import io.github.markgregg.expression.config.Configuration
import io.github.markgregg.expression.config.TypeLoader
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

object Util {
    val safeConfig = Configuration(getMockLoader())
    val evaluator = Evaluator(safeConfig)
    val tokeniser = Tokeniser(safeConfig)
    val parser = ExpressionParser(safeConfig)

    private fun getMockLoader(): TypeLoader {
        val typeLoader = mock(TypeLoader::class.java)
        whenever(typeLoader.getExtensions()).thenReturn(emptyList())
        return typeLoader
    }
}