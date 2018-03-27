package com.toxicbakery.kfinstatemachine.graph

/**
 * Graph implementation built by defining nodes via their associated edges.
 * The graph may contain one or groups of edges.
 *
 * @param mappedEdges edges of the graph grouped by their left node pointing to `n` nodes
 */
data class DirectedGraph<N, L>(
        private val mappedEdges: Map<N, Map<L, N>>
) : IDirectedGraph<N, L> {

    override val nodes: Set<N> =
            mappedEdges.values
                    .flatMap(Map<L, N>::values)
                    .plus(mappedEdges.keys)
                    .toSet()

    override fun nodeTransitions(node: N): Set<L> =
            nodeEdges(node).keys

    override fun nodeEdges(
            node: N,
            default: () -> Map<L, N>
    ): Map<L, N> = mappedEdges[node] ?: default()

}
