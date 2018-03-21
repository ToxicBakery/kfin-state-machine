package com.toxicbakery.kfinstatemachine.xml

import com.toxicbakery.kfinstatemachine.FiniteState
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

    sealed class Watch(override val id: String) : FiniteState {
        object Ready : Watch("ready")
        object Running : Watch("running")
        object Paused : Watch("paused")
        object Stopped : Watch("stopped")
    }

    @Test
    fun parseStopWatchWithMapping() {
        val machine = BasicSCXMLStreamParser::class.java.getResource("stopwatch.xml")
                .openStream()
                .let(BasicSCXMLStreamParser()::parseStream)
                .createSimpleMachineWithMapping(
                        stateMapper = { id ->
                            when (id) {
                                "ready" -> Watch.Ready
                                "running" -> Watch.Running
                                "paused" -> Watch.Paused
                                "stopped" -> Watch.Stopped
                                else -> throw Exception("Unknown state id $id")
                            }
                        },
                        transitionMapper = { event -> FiniteXmlTransition(event) }
                )

        assertEquals(Watch.Ready, machine.state)

        // Transition with only a name
        machine.performTransitionByName("watch.start")
        assertEquals(Watch.Running, machine.state)

        // Transition with a concrete transition instance
        machine.performTransition(FiniteXmlTransition("watch.stop"))
        assertEquals(Watch.Stopped, machine.state)
    }

    @Test
    fun parseMicrowave() {
        BasicSCXMLStreamParser::class.java.getResource("microwave.xml")
                .openStream()
                .let(BasicSCXMLStreamParser()::parseStream)
    }

}