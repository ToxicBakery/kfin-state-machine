package com.toxicbakery.kfinstatemachine

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface StateMachine<out F : FiniteState, T : Transition> {

    /**
     * Current state of the machine.
     */
    val state: F

    /**
     * Add a listener to be notified before a machine enters a new state.
     */
    fun addListener(listener: TransitionListener<F, T>)

    /**
     * Remove a previously added listener.
     */
    fun removeListener(listener: TransitionListener<F, T>)

    /**
     * Transition the machine.
     */
    fun performTransition(transition: T)

}