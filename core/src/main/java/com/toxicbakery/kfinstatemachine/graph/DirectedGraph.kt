package com.toxicbakery.kfinstatemachine.graph

data class DirectedGraph<N, out E>(
        override val edges: Set<GraphEdge<N, E>>
) : IDirectedGraph<N, E> {

    override val nodes: Set<GraphNode<N>> = edges.flatMap { setOf(it.left, it.right) }
            .toSet()

}
