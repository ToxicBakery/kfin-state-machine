package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.BaseMachineTest.Energy.*
import com.toxicbakery.kfinstatemachine.BaseMachineTest.EnergyTransition.*
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.Semaphore

class BaseMachineTest {

    private val directedGraph = DirectedGraph(
            edges = setOf(
                    GraphEdge(
                            left = Potential,
                            right = Kinetic,
                            label = Release
                    ),
                    GraphEdge(
                            left = Kinetic,
                            right = Potential,
                            label = Store
                    )
            )
    )

    sealed class Energy(override val id: String) : FiniteState {
        object Kinetic : Energy("Kinetic")
        object Potential : Energy("Potential")
        object InvalidState : Energy("InvalidState")
    }

    sealed class EnergyTransition(override val event: String) : Transition {
        object Store : EnergyTransition("Store")
        object Release : EnergyTransition("Release")
        object InvalidTransition : EnergyTransition("InvalidTransition")
    }

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
                    "Invalid transition Invalid for current state $Potential",
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
    fun listeners() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        val semaphore = Semaphore(0)
        val listener = machine.addListener({ _ -> semaphore.release() })
        machine.performTransition(Release)
        assertTrue(semaphore.tryAcquire())

        machine.removeListener(listener)
        machine.performTransition(Store)
        assertFalse(semaphore.tryAcquire())
    }

    @Test
    fun listenerEvent() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        machine.addListener(
                { transitionEvent ->
                    assertEquals(TransitionEvent(Release, Kinetic), transitionEvent)
                    assertEquals(Release, transitionEvent.transition)
                    assertEquals(Kinetic, transitionEvent.targetState)
                })
                .also { machine.performTransition(Release) }
                .also(machine::removeListener)
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

    @Test(expected = Exception::class)
    fun edgesForNode() {
        BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        ).edgesForNode(InvalidState)
    }

}