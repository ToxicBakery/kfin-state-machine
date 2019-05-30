package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface IStateMachine<S, T : Any> {

    /**
     * Current state of the machine.
     */
    val state: S

    /**
     * Transitions available to the current state.
     */
    val transitions: Set<KClass<out T>>

    /**
     * Transition the machine.
     */
    fun transition(transition: T)

    /**
     * Get the transition required to move to a given state.
     */
    fun transitionsTo(targetState: S): Set<KClass<out T>>

}