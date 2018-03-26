package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.BaseMachineTest.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.BaseMachineTest.Energy.Potential
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
        object Kinetic : Energy("kinetic")
        object Potential : Energy("potential")
    }

    sealed class EnergyTransition(override val event: String) : Transition {
        object Store : EnergyTransition("Store")
        object Release : EnergyTransition("Release")
        object Invalid : EnergyTransition("Invalid")
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
            assertTrue(e.message!!.startsWith("Undefined event Invalid for state "))
        }
    }

    @Test(expected = Exception::class)
    fun findNextNode() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        machine.performTransition(Invalid)
    }

    @Test
    fun listeners() {
        val machine = BaseMachine(
                directedGraph = directedGraph,
                initialState = Potential
        )

        val semaphore = Semaphore(0)
        val listener = object : TransitionListener<Energy, EnergyTransition> {
            override fun onTransition(transition: EnergyTransition, target: Energy) = semaphore.release()
        }

        machine.addListener(listener)
        machine.performTransition(Release)
        assertTrue(semaphore.tryAcquire())

        machine.removeListener(listener)
        machine.performTransition(Store)
        assertFalse(semaphore.tryAcquire())
    }

}