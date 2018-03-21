package com.toxicbakery.kfinstatemachine.xml

import org.junit.Assert.assertEquals
import org.junit.Test

class BasicSCXMLStreamParserTest {

    @Test
    fun parseStopWatch() {
        val machine = BasicSCXMLStreamParser::class.java.getResource("stopwatch.xml")
                .openStream()
                .let(BasicSCXMLStreamParser()::parseStream)
                .createSimpleMachine()

        assertEquals(FiniteXmlState("ready"), machine.state)

        // Transition with only a name
        machine.performTransitionByName("watch.start")
        assertEquals(FiniteXmlState("running"), machine.state)

        // Transition with a concrete transition instance
        machine.performTransition(FiniteXmlTransition("watch.stop"))
        assertEquals(FiniteXmlState("stopped"), machine.state)
    }

    @Test
    fun parseMicrowave() {
        BasicSCXMLStreamParser::class.java.getResource("microwave.xml")
                .openStream()
                .let(BasicSCXMLStreamParser()::parseStream)
    }

}