package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph

open class BaseMachine<F : FiniteState, T : Transition>(
        private val directedGraph: IDirectedGraph<F, T>,
        initialState: F
) : StateMachine<F, T> {

    private var node: F = directedGraph.nodes.first { it == initialState }
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
                .also { edge: GraphEdge<F, T> ->
                    moveToNode(
                            transition = edge.label,
                            nextNode = edge.right)
                }
    }

    override fun performTransition(transition: T) {
        edgeForTransitionName(transition.event)
                .also { edge: GraphEdge<F, T> ->
                    moveToNode(
                            transition = edge.label,
                            nextNode = edge.right)
                }
    }

    protected open fun moveToNode(transition: T, nextNode: F) {
        notifyListeners(transition, nextNode)
        node = nextNode
    }

    protected open fun edgeForTransitionName(event: String): GraphEdge<F, T> =
            directedGraph.mappedEdges[node]
                    ?.find { edge -> edge.label.event == event }
                    ?: throw Exception("Invalid transition $event for current state $node")

    protected open fun notifyListeners(transition: T, nextState: F) =
            listeners.forEach { transitionListener ->
                transitionListener(TransitionEvent(transition, nextState))
            }

}