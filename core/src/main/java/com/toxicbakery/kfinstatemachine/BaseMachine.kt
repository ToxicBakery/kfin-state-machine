package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph
import com.toxicbakery.kfinstatemachine.graph.exitingEdgesForNodeValue

open class BaseMachine<F : FiniteState, T : Transition>(
        private val directedGraph: IDirectedGraph<F, T>,
        initialState: F
) : StateMachine<F, T> {

    private var _state: F = initialState
    private val listeners: MutableSet<TransitionListener<F, T>> = hashSetOf()

    override val state: F
        get() = _state

    override fun addListener(listener: TransitionListener<F, T>) {
        listeners.add(listener)
    }

    override fun removeListener(listener: TransitionListener<F, T>) {
        listeners.remove(listener)
    }

    override fun performTransition(transition: T) {
        _state = findNextNode(transition)
                .let { it: GraphEdge<F, T> -> it.right.value }
                .also { nextState: F -> notifyListeners(transition, nextState) }
    }

    protected fun findNextNode(transition: T) =
            directedGraph.exitingEdgesForNodeValue(state)
                    .find { it.label == transition }
                    ?: throw Exception("Illegal transition $transition for $state")

    protected fun notifyListeners(transition: T, nextState: F) =
            listeners.forEach { it: TransitionListener<F, T> -> it.onTransition(transition, nextState) }

}