package com.toxicbakery.kfinstatemachine

import kotlin.jvm.Volatile
import kotlin.reflect.KClass

/**
 * A basic state machine that is not thread safe.
 */
open class StateMachine<S, T : Any> : IStateMachine<S, T> {

    private val transitionRules: Array<TransitionDef<S, out T>>

    private val _transitionCallbacks: MutableList<TransitionCallback<S, T>> = mutableListOf()

    /**
     * An immutable list of the currently registered callbacks.
     */
    val transitionCallbacks: List<TransitionCallback<S, T>>
        get() = _transitionCallbacks

    @Volatile
    final override var state: S

    @Suppress("UNCHECKED_CAST")
    constructor(
            initialState: S,
            vararg transitionRules: TransitionDef<S, out T>
    ) {
        state = initialState
        this.transitionRules = transitionRules as Array<TransitionDef<S, out T>>
    }

    constructor(
            initialState: S,
            transitions: List<TransitionDef<S, out T>>
    ) {
        state = initialState
        transitionRules = transitions.toTypedArray()
    }

    @Suppress("UNCHECKED_CAST")
    override val transitions: Set<KClass<out T>>
        get() = transitionRules.filter { rule -> rule.oldState == state }
                .map { rule -> rule.transition as KClass<T> }
                .toSet()

    override fun transition(transition: T) {
        val startState = state
        val edge = edge(transition)
        transitionCallbacks.forEach { cb ->
            cb.enteringState(this, startState, transition, edge.newState)
        }
        state = edge.newState
        transitionCallbacks.forEach { callback ->
            callback.enteredState(this, startState, transition, edge.newState)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun transitionsTo(targetState: S): Set<KClass<out T>> =
            transitionRules
                    .filter { rule: TransitionDef<S, *> ->
                        rule.oldState == state
                                && rule.newState == targetState
                    }
                    .map { it.transition as KClass<T> }
                    .toSet()

    /**
     * Register a callback for state transition updates.
     *
     * @param transitionCallback to be registered
     */
    fun registerCallback(transitionCallback: TransitionCallback<S, T>) =
            _transitionCallbacks.add(transitionCallback)

    /**
     * Unregister a callback from state transition updates.
     *
     * @param transitionCallback to be unregistered
     */
    fun unregisterCallback(transitionCallback: TransitionCallback<S, T>) =
            _transitionCallbacks.remove(transitionCallback)

    private fun edge(transition: Any): TransitionDef<S, *> = transitionRules
            .filter { transitionRule ->
                transitionRule.oldState == state
                        && transitionRule.transition.isInstance(transition)
            }
            .let { transitions: List<TransitionDef<S, *>> ->
                when {
                    transitions.isEmpty() ->
                        error("Invalid transition `$transition` for state `$state`.\nValid transitions ${this.transitions}")
                    transitions.size > 1 ->
                        error("Ambiguous transition `$transition` for state `$state`.\nMatches ${transitions.toTransitionsString()}.")
                    else -> transitions.first()
                }
            }

    companion object {
        private fun <S> List<TransitionDef<S, *>>.toTransitionsString(): String =
                joinToString(separator = "\n") { transitionRule ->
                    "${transitionRule.oldState} -> ${transitionRule.newState}"
                }

        fun <F, T : Any> transition(oldState: F, transition: KClass<T>, newState: F): TransitionDef<F, T> =
                TransitionDef(
                        oldState = oldState,
                        transition = transition,
                        newState = newState)
    }

}

data class TransitionDef<S, T : Any>(
        val oldState: S,
        val transition: KClass<T>,
        val newState: S
)

interface TransitionCallback<S, T : Any> {

    /**
     * After a state transition has been verified to be legal but has not yet been applied to the machine.
     *
     * @param stateMachine the machine notifying the state change
     * @param currentState the current state of the machine
     * @param transition the transition that initiated the state change
     * @param targetState the resulting state of this transition
     */
    fun enteringState(
            stateMachine: StateMachine<S, T>,
            currentState: S,
            transition: T,
            targetState: S
    )

    /**
     * After a state transition has been verified to be legal and also applied to a machine.
     *
     * @param stateMachine the machine notifying the state change
     * @param previousState the previous state of the machine before the transition was applied
     * @param transition the transition that initiated the state change
     * @param currentState the resulting state of this transition
     */
    fun enteredState(
            stateMachine: StateMachine<S, T>,
            previousState: S,
            transition: T,
            currentState: S
    )

}

/**
 * Create a transition builder for a given state and transition event.
 *
 * @param event that will trigger the transition
 */
infix fun <S, T : Any> S.onTransition(event: KClass<out T>): TransitionBuilder<S, T> =
        ConcreteTransitionBuilder(this, event)

/**
 * Define how a transition builder should end completing a fully defined transition definition.
 *
 * @param newState result of a successful transition
 */
infix fun <S, T : Any> TransitionBuilder<S, T>.resultsIn(newState: S): TransitionDef<S, out T> =
        TransitionDef(startState, event, newState)

/**
 * Base definition for a partially defined transition.
 */
interface TransitionBuilder<out S, out T : Any> {
    val startState: S
    val event: KClass<out T>
}

private class ConcreteTransitionBuilder<out S, out T : Any>(
        override val startState: S,
        override val event: KClass<out T>
) : TransitionBuilder<S, T>
