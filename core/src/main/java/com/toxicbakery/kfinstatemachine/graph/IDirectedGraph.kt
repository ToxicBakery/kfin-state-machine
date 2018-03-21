package com.toxicbakery.kfinstatemachine.graph

interface IDirectedGraph<N, out E> {
    val nodes: Set<GraphNode<N>>

    val edges: Set<GraphEdge<N, E>>

    fun nodeTransitions(node: GraphNode<N>): Set<E> =
            edges.filter { it.left == node }
                    .map { it.label }
                    .toSet()

}