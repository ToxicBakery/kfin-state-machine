package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.RxStateMachineTest.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.RxStateMachineTest.Energy.Potential
import com.toxicbakery.kfinstatemachine.RxStateMachineTest.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.RxStateMachineTest.EnergyTransition.Store
import com.toxicbakery.kfinstatemachine.StateMachine.Companion.transition
import com.toxicbakery.kfinstatemachine.TransitionEvent.ExitTransition
import org.junit.Assert.assertEquals
import org.junit.Test

class RxStateMachineTest {

    sealed class Energy {
        object Kinetic : Energy()
        object Potential : Energy()
    }

    sealed class EnergyTransition {
        object Store : EnergyTransition()
        object Release : EnergyTransition()
    }

    @Test
    fun observable() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        var currentState: Energy = stateMachine.state

        stateMachine.stateObservable
                .filter { event -> event is ExitTransition }
                .map { event -> event as ExitTransition<Energy, EnergyTransition> }
                .map { event -> event.currentState }
                .subscribe { state -> currentState = state }

        assertEquals(Potential, currentState)

        // Transition to kinetic and verify the states
        stateMachine.transition(Release)
        assertEquals(Kinetic, currentState)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitions)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitionsTo(Potential))

        // Transition back to potential and verify the states
        stateMachine.transition(Store)
        assertEquals(Potential, currentState)

        assertEquals(
                setOf(Release::class),
                stateMachine.transitions)

        assertEquals(
                setOf(Release::class),
                stateMachine.transitionsTo(Kinetic))


    }

}
