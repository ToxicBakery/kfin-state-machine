package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph

/**
 * State machine implementation dependent on a directed graph.
 *
 * @param directedGraph foundation of the machine defining the valid machine transitions and states
 * @param initialState the entry point of the machine used for determining location in the graph
 */
open class BaseMachine<F : FiniteState, T : Transition>(
        private val directedGraph: IDirectedGraph<F, T>,
        initialState: F
) : StateMachine<F, T> {

    private var node: F = directedGraph.nodes.find { it == initialState }
            ?: throw Exception("Invalid initial state $initialState not found in graph.")

    private val onTransitionListeners: MutableSet<(transitionEvent: TransitionEvent<F, T>) -> Unit> = linkedSetOf()

    private val onStateChangeListeners: MutableSet<(state: F) -> Unit> = linkedSetOf()

    override val state: F
        get() = node

    override val availableTransitions: Set<T>
        get() = directedGraph.nodeTransitions(node)

    override fun addOnStateChangeListener(listener: (state: F) -> Unit): (state: F) -> Unit =
            listener.apply { onStateChangeListeners.add(this) }

    override fun addOnTransitionListener(
            listener: (transitionEvent: TransitionEvent<F, T>) -> Unit
    ): (transitionEvent: TransitionEvent<F, T>) -> Unit =
            listener.apply { onTransitionListeners.add(this) }

    override fun removeOnStateChangedListener(listener: (state: F) -> Unit) {
        onStateChangeListeners.remove(listener)
    }

    override fun removeOnTransitionListener(listener: (transitionEvent: TransitionEvent<F, T>) -> Unit) {
        onTransitionListeners.remove(listener)
    }

    override fun performTransitionByName(event: String) {
        edgeForTransitionName(event)
                .also { (transition, node) ->
                    moveToNode(
                            transition = transition,
                            nextNode = node)
                }
    }

    override fun performTransition(transition: T) {
        edgeForTransitionName(transition.event)
                .also { (_, node) ->
                    moveToNode(
                            transition = transition,
                            nextNode = node)
                }
    }

    /**
     * Transitions the machine from the current state to a new state and notifies onTransitionListeners of the event.
     *
     * @param transition the transition that triggered the state change
     * @param nextNode the state the machine will move to once all onTransitionListeners have been notified
     */
    protected open fun moveToNode(transition: T, nextNode: F) {
        TransitionEvent(transition, nextNode)
                .also { transitionEvent ->
                    onTransitionListeners.forEach { transitionListener -> transitionListener(transitionEvent) }
                }

        node = nextNode
        onStateChangeListeners.forEach { stateListener -> stateListener(nextNode) }
    }

    /**
     * Find the GraphEdge for a given transition name or throw.
     *
     * @param event the label of the edge
     */
    protected open fun edgeForTransitionName(event: String): Map.Entry<T, F> =
            directedGraph.nodeEdges(node)
                    .entries
                    .find { entry -> entry.key.event == event }
                    ?: throw Exception("Invalid transition $event for current state $node")

}