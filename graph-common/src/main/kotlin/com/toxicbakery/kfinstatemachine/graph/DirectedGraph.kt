package com.toxicbakery.kfinstatemachine.graph

/**
 * Graph implementation built by defining nodes via their associated edges.
 * The graph may contain one or groups of edges.
 *
 * @param mappedEdges edges of the graph grouped by their left node pointing to `n` nodes
 */
open class DirectedGraph<N, E>(
        private val mappedEdges: Map<N, Map<E, N>>
) : IDirectedGraph<N, E> {

    override val nodes: Set<N> =
            mappedEdges.values
                    .flatMap(Map<E, N>::values)
                    .plus(mappedEdges.keys)
                    .toSet()

    override fun transitions(node: N): Set<E> =
            edges(node).keys

    override fun edges(
            node: N,
            defaultValue: () -> Map<E, N>
    ): Map<E, N> = mappedEdges.getOrElse(node, defaultValue)

}
