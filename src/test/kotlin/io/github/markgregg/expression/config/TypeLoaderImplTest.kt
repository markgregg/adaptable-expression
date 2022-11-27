package io.github.markgregg.expression.config

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TypeLoaderImplTest : FunSpec({

    test("getExtensions extending") {
        val typeLoader = TypeLoaderImpl("TestExtendConfig")
        typeLoader.getExtensions() shouldBe listOf(TestExtendConfig::class.java, TestExtendConfig2::class.java)
    }

    test("getExtensions override") {
        val typeLoader = TypeLoaderImpl()
        typeLoader.getExtensions() shouldBe listOf(TestConfig::class.java)
    }
})
