package com.toxicbakery.kfinstatemachine

import java.io.Serializable
import kotlin.reflect.KClass

/**
 * Full featured state machine that also implements serialization.
 */
open class SerializableStateMachine(
        /**
         * Initial state of the machine
         */
        val initialState: FiniteState
) : StateMachine, Serializable {

    private var _currentState: FiniteState = initialState

    /**
     * The current state.
     */
    val currentState: FiniteState
        get() = _currentState

    /**
     * Transition the machine to a given state.
     */
    override fun transition(transitionState: FiniteState) {
        if (_currentState == transitionState) throw IllegalTransitionToCurrentState(_currentState)
        _currentState.transitions
                .filter { transition -> transition.isInstance(transitionState) }
                .also { transitionMatches: List<KClass<out FiniteState>> ->
                    if (transitionMatches.isEmpty())
                        throw IllegalTransitionToInvalidState(_currentState, transitionState)

                    _currentState = transitionState
                }
    }

}