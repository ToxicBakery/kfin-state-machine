package com.toxicbakery.kfinstatemachine

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface StateMachine<F : FiniteState, T : Transition> {

    /**
     * Current state of the machine.
     */
    val state: F

    /**
     * Transitions available to the current state.
     */
    val availableTransitions: Set<T>

    /**
     * Add a listener to be notified before a machine enters a new state. The listener is returned for convenience.
     */
    fun addListener(
            listener: (transitionEvent: TransitionEvent<F, T>) -> Unit
    ): (transitionEvent: TransitionEvent<F, T>) -> Unit

    /**
     * Remove a previously added listener.
     */
    fun removeListener(listener: (transitionEvent: TransitionEvent<F, T>) -> Unit)

    /**
     * Transition the machine.
     */
    fun performTransitionByName(event: String)

    /**
     * Transition the machine.
     */
    fun performTransition(transition: T)

}