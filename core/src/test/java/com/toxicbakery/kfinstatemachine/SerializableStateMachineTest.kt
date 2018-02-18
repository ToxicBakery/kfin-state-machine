package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.Energy.Potential
import org.junit.Assert.assertEquals
import org.junit.Test

class SerializableStateMachineTest {

    @Test
    fun getCurrentState() {
        val serializableStateMachine = SerializableStateMachine(Potential)
        assertEquals(Potential, serializableStateMachine.initialState)
        assertEquals(Potential, serializableStateMachine.currentState)
    }

    @Test
    fun transition() {
        val serializableStateMachine = SerializableStateMachine(Potential)
        serializableStateMachine.transition(Kinetic)
        assertEquals(Kinetic, serializableStateMachine.currentState)
    }

    @Test(expected = IllegalTransitionToInvalidState::class)
    fun transitionInvalidState() {
        val serializableStateMachine = SerializableStateMachine(Potential)
        serializableStateMachine.transition(InvalidEnergy)
    }

    @Test(expected = IllegalTransitionToCurrentState::class)
    fun transitionCurrentState() {
        val serializableStateMachine = SerializableStateMachine(Potential)
        serializableStateMachine.transition(Potential)
    }

}