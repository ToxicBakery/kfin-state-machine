package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph
import com.toxicbakery.kfinstatemachine.graph.exitingEdgesForNodeValue

open class BaseMachine<F : FiniteState, T : Transition>(
        private val directedGraph: IDirectedGraph<F, T>,
        initialState: F
) : StateMachine<F, T> {

    private var node: F = directedGraph.nodes.first { it == initialState }
    private val listeners: MutableSet<TransitionListener<F, T>> = hashSetOf()

    override val state: F
        get() = node

    override val availableTransitions: Set<T>
        get() = directedGraph.nodeTransitions(node)

    override fun addListener(listener: TransitionListener<F, T>) {
        listeners.add(listener)
    }

    override fun removeListener(listener: TransitionListener<F, T>) {
        listeners.remove(listener)
    }

    override fun performTransitionByName(event: String) {
        findMatchingEdgeByTransitionName(event)
                .also { edge: GraphEdge<F, T> ->
                    moveToNode(
                            transition = edge.label,
                            nextNode = edge.right)
                }
    }

    override fun performTransition(transition: T) {
        findMatchingEdgeByTransition(transition)
                .also { edge: GraphEdge<F, T> ->
                    moveToNode(
                            transition = edge.label,
                            nextNode = edge.right)
                }
    }

    protected fun moveToNode(transition: T, nextNode: F) {
        notifyListeners(transition, nextNode)
        node = nextNode
    }

    protected fun findMatchingEdgeByTransition(transition: T) =
            directedGraph.exitingEdgesForNodeValue(state)
                    .find { it.label.event == transition.event }
                    ?: throw Exception("Illegal transition $transition for $state")

    protected fun findMatchingEdgeByTransitionName(event: String): GraphEdge<F, T> =
            directedGraph.exitingEdgesForNodeValue(state)
                    .singleOrNull { it.label.event == event }
                    ?: throw Exception("""Undefined event $event for state $node.
                        Valid events: ${directedGraph.nodeTransitions(node).joinToString { it.event }}""")

    protected fun notifyListeners(transition: T, nextState: F) =
            listeners.forEach { transitionListener -> transitionListener.onTransition(transition, nextState) }

}