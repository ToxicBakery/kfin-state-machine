package com.toxicbakery.kfinstatemachine

/**
 * Listener for transitions to new states in a machine.
 */
interface TransitionListener<in F : FiniteState, in T : Transition> {

    /**
     * Callback indicating a transition to a new state.
     */
    fun onTransition(transition: T, target: F)
}