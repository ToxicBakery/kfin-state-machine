package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.BaseMachineTest.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.BaseMachineTest.Energy.Potential
import com.toxicbakery.kfinstatemachine.BaseMachineTest.EnergyTransition.*
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.GraphNode
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.Semaphore

class BaseMachineTest {

    private val directedGraph = DirectedGraph(
            edges = setOf(
                    GraphEdge(
                            left = GraphNode(Potential),
                            right = GraphNode(Kinetic),
                            label = Release
                    ),
                    GraphEdge(
                            left = GraphNode(Kinetic),
                            right = GraphNode(Potential),
                            label = Store
                    )
            )
    )

    sealed class Energy : FiniteState {
        object Kinetic : Energy()
        object Potential : Energy()
    }

    sealed class EnergyTransition(override val name: String) : Transition {
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