package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

open class RulesBasedStateMachine<F : FiniteState>(
        initialState: F,
        private vararg val transitionRules: TransitionRule<F, *>
) : IStateMachine<F>, ITransitionable<F> {

    private var _state: F = initialState

    private val edges: Set<TransitionRule<F, *>>
        get() = transitionRules
                .filter { transitionRule -> transitionRule.oldState == _state }
                .toSet()

    @Suppress("UNCHECKED_CAST")
    private val edgeNames: Set<Class<F>>
        get() = edges
                .map { it.transition.java as Class<F> }
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
                    .performAction(transition)

    override fun transitionsTo(targetState: F): Set<KClass<*>> =
            transitionRules
                    .filter {
                        it.oldState == _state
                                && it.newState == targetState
                    }
                    .map { it.transition }
                    .toSet()

    @Suppress("UNCHECKED_CAST")
    private fun edge(transition: Any) = edges
            .filter { it.transition.java.isInstance(transition) && it.validate(transition) }
            .singleOrNull()
            ?: throw Exception("Invalid transition `${transition.javaClass.simpleName}` for state `$_state`.\nValid edges: $edgeNames")

    companion object {
        fun <F, T : Any> transition(oldState: F, transition: KClass<T>, newState: F): TransitionRule<F, T> =
                TransitionRule(
                        oldState = oldState,
                        transition = transition,
                        newState = newState)
    }

}

data class TransitionRule<S, T : Any>(
        val oldState: S,
        val transition: KClass<T>,
        val newState: S,
        private val _onlyIf: (transition: T) -> Boolean = { _ -> true },
        private val _reaction: (transition: T) -> Unit = { _ -> }
) {

    fun onlyIf(func: (transition: T) -> Boolean): TransitionRule<S, T> = copy(_onlyIf = func)

    fun reaction(func: (transition: T) -> Unit): TransitionRule<S, T> = copy(_reaction = func)

    @Suppress("UNCHECKED_CAST")
    internal fun validate(transition: Any) = _onlyIf(transition as T)

    @Suppress("UNCHECKED_CAST")
    internal fun performAction(transition: Any) = _reaction(transition as T)

}