package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.RxMachineTest.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.RxMachineTest.Energy.Potential
import com.toxicbakery.kfinstatemachine.RxMachineTest.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.RxMachineTest.EnergyTransition.Store
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class RxMachineTest {

    sealed class Energy(override val id: String) : FiniteState {
        object Kinetic : Energy("kinetic")
        object Potential : Energy("potential")
    }

    sealed class EnergyTransition(override val event: String) : Transition {
        object Store : EnergyTransition("Store")
        object Release : EnergyTransition("Release")
    }

    private val energyDirectedGraph = DirectedGraph(
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

    @Test
    fun stateObservableTest() {
        val machine = BaseMachine(energyDirectedGraph, Potential)
        val expectedStates = LinkedList(
                listOf(
                        Potential,
                        Kinetic
                )
        )

        val disposable = machine.stateObservable
                .subscribe { energy: Energy -> assertEquals(expectedStates.poll(), energy) }

        machine.performTransition(Release)
        assertEquals(0, expectedStates.size)
        disposable.dispose()
    }

    @Test
    fun transitionObservableTest() {
        val machine = BaseMachine(energyDirectedGraph, Potential)
        val expectedTransitions = LinkedList(
                listOf(
                        TransitionEvent(Release, Kinetic),
                        TransitionEvent(Store, Potential)
                )
        )

        val disposable = machine.transitionObservable
                .subscribe { transitionEvent -> assertEquals(expectedTransitions.poll(), transitionEvent) }

        machine.performTransition(Release)
        machine.performTransition(Store)
        assertEquals(0, expectedTransitions.size)
        disposable.dispose()
    }

}