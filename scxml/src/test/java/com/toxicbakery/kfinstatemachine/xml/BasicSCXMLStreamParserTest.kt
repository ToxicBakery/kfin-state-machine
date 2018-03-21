package com.toxicbakery.kfinstatemachine.xml

import com.toxicbakery.kfinstatemachine.FiniteState
import com.toxicbakery.kfinstatemachine.StateMachine
import com.toxicbakery.kfinstatemachine.Transition
import org.junit.Assert.*
import org.junit.Test

class BasicSCXMLStreamParserTest {

    private val stopWatchMachine: StateMachine<FiniteState, Transition>
        get() = BasicSCXMLStreamParser::class.java.getResource("stopwatch.xml")
                .openStream()
                .let(BasicSCXMLStreamParser()::parseStream)
                .createRootMachine()
                .let { it["root"]!! }

    @Test
    fun parseStopWatch() {
        val machine = stopWatchMachine
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
                .createRootMachine()
    }

}