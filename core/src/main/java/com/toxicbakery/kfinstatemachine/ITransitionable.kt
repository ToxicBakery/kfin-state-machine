package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

interface ITransitionable<in F> {

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
    fun transitionsTo(targetState: F): Set<KClass<*>>

}