package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

open class StateMachine<F : FiniteState>(
        initialState: F,
        private vararg val transitionRules: TransitionRule<F, *>
) : IStateMachine<F> {

    private var _state: F = initialState

    private val edges: Set<TransitionRule<F, *>>
        get() = transitionRules
                .filter { transitionRule -> transitionRule.oldState == _state }
                .toSet()

    override val state: F
        get() = _state

    override val transitions: Set<KClass<*>>
        get() = transitionRules.filter { it.oldState == _state }
                .map { it.transition }
                .toSet()

    override fun transition(transition: Any): Unit =
            edge(transition)
                    .also { _state = it.newState }
                    .performReactions(this, transition)

    override fun transitionsTo(targetState: F): Set<KClass<*>> =
            transitionRules
                    .filter { rule: TransitionRule<F, *> ->
                        rule.oldState == _state
                                && rule.newState == targetState
                    }
                    .map(TransitionRule<F, *>::transition)
                    .toSet()

    @Suppress("UNCHECKED_CAST")
    private fun edge(transition: Any) = edges
            .singleOrNull { it.transition.java.isInstance(transition) && it.validate(transition) }
            ?: throw Exception("Invalid transition `${transition.javaClass.simpleName}` for state `$_state`.")

    companion object {
        fun <F : FiniteState, T : Any> transition(oldState: F, transition: KClass<T>, newState: F): TransitionRule<F, T> =
                TransitionRule(
                        oldState = oldState,
                        transition = transition,
                        newState = newState)
    }

}

data class TransitionRule<F : FiniteState, T : Any>(
        val oldState: F,
        val transition: KClass<T>,
        val newState: F,
        private val validations: List<(transition: T) -> Boolean> = listOf(),
        private val reactions: List<(machine: StateMachine<F>, transition: T) -> Unit> = listOf()
) {

    fun onlyIf(func: (transition: T) -> Boolean): TransitionRule<F, T> =
            copy(validations = validations.plus(func))

    fun reaction(func: (machine: StateMachine<F>, transition: T) -> Unit): TransitionRule<F, T> =
            copy(reactions = reactions.plus(func))

    @Suppress("UNCHECKED_CAST")
    internal fun validate(transition: Any) = validations
            .fold(true, { acc: Boolean, validation: (transition: T) -> Boolean ->
                acc && validation(transition as T)
            })

    @Suppress("UNCHECKED_CAST")
    internal fun performReactions(machine: StateMachine<F>, transition: Any) = reactions
            .forEach { func -> func(machine, transition as T) }

}