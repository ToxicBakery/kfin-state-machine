package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.RxStateMachineTest.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.RxStateMachineTest.Energy.Potential
import com.toxicbakery.kfinstatemachine.RxStateMachineTest.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.RxStateMachineTest.EnergyTransition.Store
import com.toxicbakery.kfinstatemachine.StateMachine.Companion.transition
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

        var lastEnterState: Energy = stateMachine.state
        var lastExitState: Energy = stateMachine.state

        val enterDisposable = stateMachine.enterTransitionObservable
                .map { event -> event.currentState }
                .subscribe { state -> lastEnterState = state }

        val exitDisposable = stateMachine.exitTransitionObservable
                .map { event -> event.currentState }
                .subscribe { state -> lastExitState = state }

        // Transition to kinetic and verify the states
        stateMachine.transition(Release)
        assertEquals(Potential, lastEnterState)
        assertEquals(Kinetic, lastExitState)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitions)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitionsTo(Potential))

        // Transition back to potential and verify the states
        stateMachine.transition(Store)
        assertEquals(Kinetic, lastEnterState)
        assertEquals(Potential, lastExitState)

        assertEquals(
                setOf(Release::class),
                stateMachine.transitions)

        assertEquals(
                setOf(Release::class),
                stateMachine.transitionsTo(Kinetic))

        // Cleanup
        assertEquals(2, stateMachine.transitionCallbacks.size)
        enterDisposable.dispose()
        exitDisposable.dispose()
        assertEquals(0, stateMachine.transitionCallbacks.size)
    }

}
