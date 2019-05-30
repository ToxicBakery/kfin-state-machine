package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.Energy.Potential
import com.toxicbakery.kfinstatemachine.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.EnergyTransition.Store
import com.toxicbakery.kfinstatemachine.StateMachine.Companion.transition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class StateMachineTest {

    @Test
    fun performTransition() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(Potential, stateMachine.state)

        stateMachine.transition(Release)
        assertEquals(Kinetic, stateMachine.state)

        stateMachine.transition(Store)
        assertEquals(Potential, stateMachine.state)
    }

    @Test
    fun performTransition_withList() {
        val stateMachine = StateMachine(
                Potential,
                listOf(
                        transition(Potential, Release::class, Kinetic),
                        transition(Kinetic, Store::class, Potential
                        )))

        assertEquals(Potential, stateMachine.state)

        stateMachine.transition(Release)
        assertEquals(Kinetic, stateMachine.state)

        stateMachine.transition(Store)
        assertEquals(Potential, stateMachine.state)
    }

    @Test
    fun performTransition_withInfixFunctions() {
        val stateMachine = StateMachine(
                Potential,
                Potential onTransition Release::class resultsIn Kinetic,
                Kinetic onTransition Store::class resultsIn Potential)

        assertEquals(Potential, stateMachine.state)

        stateMachine.transition(Release)
        assertEquals(Kinetic, stateMachine.state)

        stateMachine.transition(Store)
        assertEquals(Potential, stateMachine.state)
    }

    @Test
    fun performTransition_invalidTransition() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        try {
            stateMachine.transition(Store)
            fail("Exception expected")
        } catch (e: Exception) {
            assertTrue(e.message?.startsWith("Invalid transition ") ?: false)
        }
    }

    @Test
    fun performTransition_ambiguousTransition() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition<Energy, Release>(Potential, Release::class, Potential),
                transition(Kinetic, Store::class, Potential))

        try {
            stateMachine.transition(Release)
            fail("Exception expected")
        } catch (e: Exception) {
            assertTrue(e.message?.startsWith("Ambiguous transition ") ?: false)
        }
    }

    @Test
    fun availableTransitions() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(
                setOf(Release::class),
                stateMachine.transitions)

        stateMachine.transition(Release)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitions)
    }

    @Test
    fun transitionsForTargetState() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(
                setOf(Release::class),
                stateMachine.transitionsTo(Kinetic))

        assertEquals(
                setOf(),
                stateMachine.transitionsTo(Potential))

        stateMachine.transition(Release)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitionsTo(Potential))

        assertEquals(
                setOf(),
                stateMachine.transitionsTo(Kinetic))
    }

}