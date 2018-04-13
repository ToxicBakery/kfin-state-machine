package com.toxicbakery.kfinstatemachine

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface IStateMachine<F, T> {

    /**
     * Current state of the machine.
     */
    val state: F

    /**
     * Transitions available to the current state.
     */
    val availableTransitions: Set<T>

    /**
     * Add a listener to be notified after a machine enters a new state. The listener is returned for convenience.
     */
    fun addOnStateChangeListener(
            listener: (state: F) -> Unit
    ): (state: F) -> Unit

    /**
     * Add a listener to be notified before a machine enters a new state. The listener is returned for convenience.
     */
    fun addOnTransitionListener(
            listener: (transitionEvent: TransitionEvent<F, T>) -> Unit
    ): (transitionEvent: TransitionEvent<F, T>) -> Unit

    /**
     * Remove a previously added listener.
     */
    fun removeOnStateChangedListener(listener: (state: F) -> Unit)

    /**
     * Remove a previously added listener.
     */
    fun removeOnTransitionListener(listener: (transitionEvent: TransitionEvent<F, T>) -> Unit)

    /**
     * Transition the machine.
     */
    fun performTransitionByName(event: String)

    /**
     * Transition the machine.
     */
    fun performTransition(transition: T)

    /**
     * Get the transition required to move to a given state.
     */
    fun transitionForTargetState(targetState: F): T

}