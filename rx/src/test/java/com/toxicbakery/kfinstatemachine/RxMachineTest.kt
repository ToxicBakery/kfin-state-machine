package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.RxMachineTest.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.RxMachineTest.Energy.Potential
import com.toxicbakery.kfinstatemachine.RxMachineTest.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.RxMachineTest.EnergyTransition.Store
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

class RxMachineTest {

    sealed class Energy(override val id: String) : FiniteState {
        object Kinetic : Energy("kinetic")
        object Potential : Energy("potential")
    }

    sealed class EnergyTransition {
        object Store : EnergyTransition()
        object Release : EnergyTransition()
    }

    private val energyDirectedGraph: IDirectedGraph<Energy, KClass<*>> = DirectedGraph(
            mappedEdges = mapOf(
                    Potential to mapOf<KClass<*>, Energy>(Release::class to Kinetic),
                    Kinetic to mapOf<KClass<*>, Energy>(Store::class to Potential)
            )
    )

    @Test
    fun stateObservableTest() {
        val machine = StateMachine(energyDirectedGraph, Potential)
        val expectedStates = mutableListOf(
                Potential,
                Kinetic
        )

        val disposable = machine.stateChangeObservable
                .subscribe { energy: Energy -> assertEquals(expectedStates.removeAt(0), energy) }

        machine.transition(Release)
        assertEquals(0, expectedStates.size)
        disposable.dispose()
    }

    @Test
    fun transitionObservableTest() {
        val machine = StateMachine(energyDirectedGraph, Potential)
        val expectedTransitions = mutableListOf(
                TransitionEvent(Release, Kinetic),
                TransitionEvent(Store, Potential)
        )

        val disposable = machine.transitionEventObservable
                .subscribe { transitionEvent -> assertEquals(expectedTransitions.removeAt(0), transitionEvent) }

        machine.transition(Release)
        machine.transition(Store)
        assertEquals(0, expectedTransitions.size)
        disposable.dispose()
    }

}