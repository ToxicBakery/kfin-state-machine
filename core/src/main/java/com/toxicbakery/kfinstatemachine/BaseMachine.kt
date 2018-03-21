package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.GraphNode
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph
import com.toxicbakery.kfinstatemachine.graph.exitingEdgesForNodeValue

open class BaseMachine<F : FiniteState, T : Transition>(
        private val directedGraph: IDirectedGraph<F, T>,
        initialState: F
) : StateMachine<F, T> {

    private var node: GraphNode<F> = directedGraph.nodes.first { it.value == initialState }
    private val listeners: MutableSet<TransitionListener<F, T>> = hashSetOf()

    override val state: F
        get() = node.value

    override fun addListener(listener: TransitionListener<F, T>) {
        listeners.add(listener)
    }

    override fun removeListener(listener: TransitionListener<F, T>) {
        listeners.remove(listener)
    }

    override fun performTransitionByName(event: String) {
        getMatchingNodeByTransitionName(event)
                .label
                .let(this::performTransition)
    }

    override fun performTransition(transition: T) {
        node = findNextNode(transition)
                .let { it: GraphEdge<F, T> -> it.right }
                .also { nextNode: GraphNode<F> -> notifyListeners(transition, nextNode.value) }
    }

    protected fun findNextNode(transition: T) =
            directedGraph.exitingEdgesForNodeValue(state)
                    .find { it.label == transition }
                    ?: throw Exception("Illegal transition $transition for $state")

    protected fun notifyListeners(transition: T, nextState: F) =
            listeners.forEach { it: TransitionListener<F, T> -> it.onTransition(transition, nextState) }

    private fun getMatchingNodeByTransitionName(event: String) =
            directedGraph.edges
                    .singleOrNull { it.left == node && it.label.event == event }
                    ?: throw Exception("""Undefined event $event for state ${node.value}.
                        Valid events: ${directedGraph.nodeTransitions(node).joinToString { it.event }}""")

}