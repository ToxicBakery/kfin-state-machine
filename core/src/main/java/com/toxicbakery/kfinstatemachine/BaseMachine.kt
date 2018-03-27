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

    private val listeners: MutableSet<(transitionEvent: TransitionEvent<F, T>) -> Unit> = hashSetOf()

    override val state: F
        get() = node

    override val availableTransitions: Set<T>
        get() = directedGraph.nodeTransitions(node)

    override fun addListener(
            listener: (transitionEvent: TransitionEvent<F, T>) -> Unit
    ): (transitionEvent: TransitionEvent<F, T>) -> Unit =
            listener.apply { listeners.add(this) }

    override fun removeListener(listener: (transitionEvent: TransitionEvent<F, T>) -> Unit) {
        listeners.remove(listener)
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
                .also { (transition, node) ->
                    moveToNode(
                            transition = transition,
                            nextNode = node)
                }
    }

    /**
     * Transitions the machine from the current state to a new state and notifies listeners of the event.
     *
     * @param transition the transition that triggered the state change
     * @param nextNode the state the machine will move to once all listeners have been notified
     */
    protected open fun moveToNode(transition: T, nextNode: F) {
        notifyListeners(TransitionEvent(transition, nextNode))
        node = nextNode
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

    /**
     * Notify listeners of a transition.
     *
     * @param transitionEvent event describing the pending state change
     */
    protected open fun notifyListeners(transitionEvent: TransitionEvent<F, T>) =
            listeners.forEach { transitionListener -> transitionListener(transitionEvent) }

}