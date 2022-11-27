package io.github.markgregg.expression.config

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.github.markgregg.expression.Util

class ConfigurationTest : FunSpec({

    test("functionStart") {
        Util.safeConfig.functionStart shouldBe  '('
    }

    test("functionEnd") {
        Util.safeConfig.functionEnd shouldBe  ')'
    }

    test("functionSeparator") {
        Util.safeConfig.functionSeparator shouldBe  ','
    }

    test("scopeStart") {
        Util.safeConfig.scopeStart shouldBe  '('
    }

    test("scopeEnd") {
        Util.safeConfig.scopeEnd shouldBe  ')'
    }

    test("dateIdentifier") {
        Util.safeConfig.dateIdentifier shouldBe  '#'
    }

    test("stringIdentifier") {
        Util.safeConfig.stringIdentifier shouldBe  '\''
    }

    test("getSingleElement") {
        listOf(
            '.','[','$','?','>','<','+','-','*','/','%','!'
        ).forEach {
            Util.safeConfig.getSingleElement(it).shouldNotBeNull()
        }
    }

    test("getDualElement") {
        listOf(
            "&&", "||", "==", "!=", ">=", "<="
        ).forEach {
            Util.safeConfig.getDualElement(it).shouldNotBeNull()
        }

    }

    test("getFunction") {
        listOf(
            "left","right","substr","lcase","ucase","trim","trimstart","trimend",
            "tostr","tolng","todbl","tobool","todate","todatetime","today","now"
        ).forEach {
            Util.safeConfig.getFunction(it).shouldNotBeNull()
        }

    }

    test( "extensions extend") {
        val config = Configuration(TypeLoaderImpl("TestExtendConfig"))
        config.getFunction("test").shouldNotBeNull()
        config.getTripleElement("---").shouldNotBeNull()
        config.getSingleElement('.').shouldNotBeNull()
        config.getDualElement("==").shouldNotBeNull()
    }

    test( "extensions override") {
        Configuration.instance.getFunction("test").shouldNotBeNull()
        Configuration.instance.getTripleElement("---").shouldNotBeNull()
        Configuration.instance.getSingleElement('.').shouldBeNull()
        Configuration.instance.getDualElement("==").shouldBeNull()
        Configuration.instance.functionStart shouldBe  '*'
        Configuration.instance.functionEnd shouldBe  '&'
        Configuration.instance.functionSeparator shouldBe  '~'
        Configuration.instance.scopeStart shouldBe  '['
        Configuration.instance.scopeEnd shouldBe  ']'
        Configuration.instance.dateIdentifier shouldBe '%'
        Configuration.instance.stringIdentifier shouldBe  '"'
    }

})
