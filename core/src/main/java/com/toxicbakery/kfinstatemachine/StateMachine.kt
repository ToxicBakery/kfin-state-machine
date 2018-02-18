package com.toxicbakery.kfinstatemachine

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface StateMachine {

    /**
     * Transition the machine to a given state.
     */
    fun transition(transitionState: FiniteState)
}