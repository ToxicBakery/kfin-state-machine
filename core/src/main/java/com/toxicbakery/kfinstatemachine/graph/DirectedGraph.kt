package com.toxicbakery.kfinstatemachine.graph

/**
 * Graph implementation built by defining nodes via their associated edges.
 * The graph may contain one or groups of edges.
 *
 * @param edges basis of the graph defining the nodes and how they relate to each other
 */
data class DirectedGraph<N, out L>(
        override val edges: Set<GraphEdge<N, L>>
) : IDirectedGraph<N, L> {

    override val mappedEdges: Map<N, List<GraphEdge<N, L>>> =
            edges.groupBy { it.left }

    override val nodes: Set<N> =
            edges.flatMap { setOf(it.left, it.right) }
                    .toSet()

    init {
        edges.groupBy { Pair(it.left, it.label) }
                .map { it.value }
                .firstOrNull { it.size > 1 }
                ?.let { offendingEdges: List<GraphEdge<N, L>> ->
                    val (leftNode, label) = offendingEdges.first()
                            .let { Pair(it.left, it.label) }
                    val ambiguousNodes = offendingEdges.map { it.right }
                    throw Exception("""Ambiguous edges detected for $leftNode with edge $label.
                    Destination nodes found $ambiguousNodes""")
                }
    }

    override fun nodeTransitions(node: N): Set<L> =
            getEdgesForNode(node)
                    .map { it.label }
                    .toSet()

    private fun getEdgesForNode(node: N): List<GraphEdge<N, L>> =
            mappedEdges[node] ?: throw Exception("Node not in graph.")

}
