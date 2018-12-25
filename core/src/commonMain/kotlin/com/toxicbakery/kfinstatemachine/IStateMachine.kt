package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface IStateMachine<S> {

    /**
     * Current state of the machine.
     */
    val state: S

    /**
     * Transitions available to the current state.
     */
    val transitions: Set<KClass<*>>

    /**
     * Transition the machine.
     */
    fun transition(transition: Any)

    /**
     * Get the transition required to move to a given state.
     */
    fun transitionsTo(targetState: S): Set<KClass<*>>

}