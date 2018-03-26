package com.toxicbakery.kfinstatemachine.graph

data class DirectedGraph<N, out E>(
        override val edges: Set<GraphEdge<N, E>>
) : IDirectedGraph<N, E> {

    override val nodes: Set<N> = edges.flatMap { setOf(it.left, it.right) }
            .toSet()

    init {
        edges.groupBy { Pair(it.left, it.label) }
                .map { it.value }
                .firstOrNull { it.size > 1 }
                ?.let { offendingEdges: List<GraphEdge<N, E>> ->
                    val (leftNode, label) = offendingEdges.first()
                            .let { Pair(it.left, it.label) }
                    val ambiguousNodes = offendingEdges.map { it.right }
                    throw Exception("""Ambiguous edges detected for $leftNode with edge $label.
                    Destination nodes found $ambiguousNodes""")
                }
    }

}
