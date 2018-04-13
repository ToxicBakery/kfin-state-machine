package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph
import kotlin.reflect.KClass

/**
 * State machine implementation dependent on a directed graph.
 *
 * @param directedGraph foundation of the machine defining the valid machine transitions and states
 * @param initialState the entry point of the machine used for determining location in the graph
 */
open class StateMachine<F : FiniteState>(
        val directedGraph: IDirectedGraph<F, KClass<*>>,
        initialState: F
) : IStateMachine<F>, ITransitionable<F> {

    private var node: F = directedGraph.nodes.find { it == initialState }
            ?: throw Exception("Invalid initial state $initialState not found in graph.")

    private val onTransitionListeners: MutableSet<(transitionEvent: TransitionEvent<F>) -> Unit> = linkedSetOf()

    private val onStateChangeListeners: MutableSet<(state: F) -> Unit> = linkedSetOf()

    override val state: F
        get() = node

    override val availableTransitions: Set<KClass<*>>
        get() = directedGraph.nodeTransitions(node)

    /**
     * Add a listener to be notified after a machine enters a new state. The listener is returned for convenience.
     */
    fun addOnStateChangeListener(listener: (state: F) -> Unit): (state: F) -> Unit =
            listener.apply { onStateChangeListeners.add(this) }

    /**
     * Add a listener to be notified before a machine enters a new state. The listener is returned for convenience.
     */
    fun addOnTransitionListener(
            listener: (transitionEvent: TransitionEvent<F>) -> Unit
    ): (transitionEvent: TransitionEvent<F>) -> Unit =
            listener.apply { onTransitionListeners.add(this) }

    /**
     * Remove a previously added listener.
     */
    fun removeOnStateChangedListener(listener: (state: F) -> Unit) {
        onStateChangeListeners.remove(listener)
    }

    /**
     * Remove a previously added listener.
     */
    fun removeOnTransitionListener(listener: (transitionEvent: TransitionEvent<F>) -> Unit) {
        onTransitionListeners.remove(listener)
    }

    override fun performTransition(transition: Any) {
        directedGraph.nodeEdges(node)
                .entries
                .single { it.key == transition.javaClass.kotlin }
                .also { (_, node) ->
                    moveToNode(
                            transition = transition,
                            nextNode = node)
                }
    }

    override fun transitionsForTargetState(targetState: F): Set<KClass<*>> =
            directedGraph.nodeEdges(node)
                    .entries
                    .filter { it.value == targetState }
                    .map { it.key }
                    .toSet()

    /**
     * Transitions the machine from the current state to a new state and notifies onTransitionListeners of the event.
     *
     * @param transition the transition that triggered the state change
     * @param nextNode the state the machine will move to once all onTransitionListeners have been notified
     */
    protected open fun moveToNode(transition: Any, nextNode: F) {
        TransitionEvent(transition, nextNode)
                .also { transitionEvent ->
                    onTransitionListeners.forEach { transitionListener ->
                        transitionListener(transitionEvent)
                    }
                }

        node = nextNode
        onStateChangeListeners.forEach { stateListener -> stateListener(nextNode) }
    }

}