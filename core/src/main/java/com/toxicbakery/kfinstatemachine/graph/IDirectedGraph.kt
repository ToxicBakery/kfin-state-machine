package com.toxicbakery.kfinstatemachine.graph

interface IDirectedGraph<N, out E> {
    val nodes: Set<N>

    val edges: Set<GraphEdge<N, E>>

    fun nodeTransitions(node: N): Set<E> =
            edges.filter { it.left == node }
                    .map { it.label }
                    .toSet()

}