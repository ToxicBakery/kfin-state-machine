package com.toxicbakery.kfinstatemachine.xml

import org.junit.Test

class BasicSCXMLStreamParserTest {

    @Test
    fun parseStopWatch() {
        BasicSCXMLStreamParser::class.java.getResource("stopwatch.xml")
                .openStream()
                .let(BasicSCXMLStreamParser()::parseStream)
                .let { println(it) }
    }

    @Test
    fun parseMicrowave() {
        BasicSCXMLStreamParser::class.java.getResource("microwave.xml")
                .openStream()
                .let(BasicSCXMLStreamParser()::parseStream)
                .let { println(it) }
    }

}