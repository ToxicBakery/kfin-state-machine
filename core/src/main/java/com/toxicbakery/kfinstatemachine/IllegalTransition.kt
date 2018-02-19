package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.debug.createAsciiTable

/**
 * Exception representing a machine attempting to change to it's current state.
 */
data class IllegalTransitionToCurrentState(
        /**
         * The current state of the machine
         */
        private val state: FiniteState
) : Exception("Illegal attempt to transition to current state $state")

/**
 * Exception representing a machine attempting to change to a state out of sequence of the current state.
 */
data class IllegalTransitionToInvalidState(
        /**
         * The current state of the machine
         */
        private val currentState: FiniteState,

        /**
         * The attempted transition state
         */
        private val transitionState: FiniteState
) : Exception("Illegal attempt to transition from $currentState to $transitionState.\n${currentState.debugTable()}")

/**
 * Create a table of the state and valid transitions for debugging.
 */
fun FiniteState.debugTable(): String =
        mutableListOf(javaClass.simpleName)
                .apply { transitions.forEach { add(it.simpleName) } }
                .let(::createAsciiTable)