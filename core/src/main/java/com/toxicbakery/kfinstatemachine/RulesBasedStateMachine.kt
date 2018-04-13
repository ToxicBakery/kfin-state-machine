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

    private val edgeNames: Set<String>
        get() = edges
                .map { it.transition.java.simpleName }
                .toSet()

    override val state: F
        get() = _state

    override val availableTransitions: Set<KClass<*>>
        get() = transitionRules.filter { it.oldState == _state }
                .map { it.transition }
                .toSet()

    override fun performTransition(transition: Any) {
        _state = edges
                .filter { it.transition.java.isInstance(transition) }
                .singleOrNull { it.transition(this, transition) }
                ?.newState
                ?: throw Exception("Invalid transition `$transition` for state `$_state`.\nValid edges: $edgeNames")
    }

    override fun transitionsForTargetState(targetState: F): Set<KClass<*>> =
            transitionRules
                    .filter {
                        it.oldState == _state
                                && it.newState == targetState
                    }
                    .map { it.transition }
                    .toSet()

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
        private val onlyIf: (transition: T) -> Boolean = { true },
        private val doAction: (transitionable: ITransitionable<S>, transition: T) -> Unit = { _, _ -> }
) {

    fun onlyIf(func: (transition: T) -> Boolean): TransitionRule<S, T> = copy(onlyIf = func)

    fun doAction(func: (transitionable: ITransitionable<S>, transition: T) -> Unit): TransitionRule<S, T> =
            copy(doAction = func)

    @Suppress("UNCHECKED_CAST")
    fun transition(transitionable: ITransitionable<S>, transition: Any): Boolean =
            onlyIf(transition as T).apply {
                if (this) doAction(transitionable, transition)
            }

}