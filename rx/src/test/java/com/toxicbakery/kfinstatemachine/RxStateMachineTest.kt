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
                .let { stateMachine -> RxStateMachine(stateMachine) }

        assertEquals(Potential, stateMachine.observable.blockingFirst())

        stateMachine.transition(Release)
        assertEquals(Kinetic, stateMachine.observable.blockingFirst())

        assertEquals(
                setOf(Store::class),
                stateMachine.transitions)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitionsTo(Potential))
    }

}