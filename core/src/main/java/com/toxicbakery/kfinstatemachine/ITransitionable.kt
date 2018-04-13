package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

interface ITransitionable<in F> {

    /**
     * Transitions available to the current state.
     */
    val availableTransitions: Set<KClass<*>>

    /**
     * Transition the machine.
     */
    fun performTransition(transition: Any)

    /**
     * Get the transition required to move to a given state.
     */
    fun transitionsForTargetState(targetState: F): Set<KClass<*>>

}