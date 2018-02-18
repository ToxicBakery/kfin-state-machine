package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.Energy.Potential
import org.junit.Assert.assertEquals
import org.junit.Test

class RxSerializableStateMachineTest {

    @Test
    fun getCurrentStateObservable() {
        val serializableStateMachine = RxSerializableStateMachine(Potential)
        assertEquals(Potential, serializableStateMachine.initialState)
        assertEquals(Potential, serializableStateMachine.currentStateObservable.blockingFirst())
    }

    @Test
    fun transition() {
        val serializableStateMachine = RxSerializableStateMachine(Potential)
        serializableStateMachine.transition(Kinetic)
        assertEquals(Kinetic, serializableStateMachine.currentStateObservable.blockingFirst())
    }

    @Test(expected = IllegalTransitionToInvalidState::class)
    fun transitionInvalidState() {
        val serializableStateMachine = RxSerializableStateMachine(Potential)
        serializableStateMachine.transition(InvalidEnergy)
    }

    @Test(expected = IllegalTransitionToCurrentState::class)
    fun transitionCurrentState() {
        val serializableStateMachine = RxSerializableStateMachine(Potential)
        serializableStateMachine.transition(Potential)
    }

}