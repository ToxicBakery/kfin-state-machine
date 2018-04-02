package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.Energy.*
import com.toxicbakery.kfinstatemachine.EnergyTransition.*
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.Semaphore

class BaseMachineTest {

    private val directedGraph: IDirectedGraph<Energy, EnergyTransition> = DirectedGraph(
            mappedEdges = mapOf(
                    Potential to mapOf<EnergyTransition, Energy>(Release to Kinetic),
                    Kinetic to mapOf<EnergyTransition, Energy>(Store to Potential)
            )
    )

    @Test
    fun performTransition() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        machine.performTransition(Release)
        assertEquals(Kinetic, machine.state)
    }

    @Test
    fun performTransitionByName() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        machine.performTransitionByName("Release")
        assertEquals(Kinetic, machine.state)
    }

    @Test
    fun performTransitionByNameWithInvalidName() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        try {
            machine.performTransitionByName("Invalid")
            fail("Expected exception caused by invalid name.")
        } catch (e: Exception) {
            assertEquals(
                    "Invalid transition Invalid for current state ${Potential.id}",
                    e.message
            )
        }
    }

    @Test(expected = Exception::class)
    fun findNextNode() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        machine.performTransition(InvalidTransition)
    }

    @Test
    fun onTransitionListener() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        val semaphore = Semaphore(0)
        val listener = machine.addOnTransitionListener { _ -> semaphore.release() }
        machine.performTransition(Release)
        assertTrue(semaphore.tryAcquire())

        machine.removeOnTransitionListener(listener)
        assertFalse(semaphore.tryAcquire())
    }

    @Test
    fun onStateChangeListener() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        val semaphore = Semaphore(0)
        val listener = machine.addOnStateChangeListener { _ -> semaphore.release() }
        machine.performTransition(Release)
        assertTrue(semaphore.tryAcquire())

        machine.removeOnStateChangedListener(listener)
        assertFalse(semaphore.tryAcquire())
    }

    @Test
    fun transitionEvent() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        machine.addOnTransitionListener(
                { transitionEvent ->
                    assertEquals(TransitionEvent(Release, Kinetic), transitionEvent)
                    assertEquals(Release, transitionEvent.transition)
                    assertEquals(Kinetic, transitionEvent.targetState)
                })
                .also { machine.performTransition(Release) }
                .also(machine::removeOnTransitionListener)
    }

    @Test
    fun availableTransitions() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        assertEquals(
                setOf(Release),
                machine.availableTransitions
        )
    }

    @Test(expected = Exception::class)
    fun initialStateOutsideGraph() {
        BaseMachine(
                directedGraph = directedGraph,
                initialState = InvalidState
        )
    }

}