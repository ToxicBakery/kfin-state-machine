package com.toxicbakery.kfinstatemachine.graph

/**
 * Graph implementation built by defining nodes via their associated edges.
 * The graph may contain one or groups of edges.
 *
 * @param mappedEdges edges of the graph grouped by their left node pointing to `n` nodes
 */
open class DirectedGraph<N, E> : IDirectedGraph<N, E> {

    private val mappedEdges: List<Pair<N, Map<E, N>>>

    constructor(mappedEdges: List<Pair<N, Map<E, N>>>) {
        this.mappedEdges = mappedEdges
        this.nodes = mappedEdges
            .flatMap {
                mappedEdges.flatMap { (_, edgeMap) ->
                    edgeMap.values
                }
            }
            .plus(mappedEdges.map { (node, _) -> node })
            .toSet()
    }

    constructor(mappedEdges: Map<N, Map<E, N>>) : this(mappedEdges.toList())

    override val nodes: Set<N>

    override fun transitions(node: N): Set<E> =
        edges(node).keys

    override fun edges(
        node: N,
        defaultValue: () -> Map<E, N>
    ): Map<E, N> = mappedEdges
        .filter { (n, _) -> n == node }
        .flatMap { (_, edge) -> edge.entries.map { entry -> entry.key to entry.value } }
        .toMap()
        .let { map -> if (map.isEmpty()) defaultValue() else map }

    /**
     * Source graph defining this graph instance.
     */
    fun graph(): List<Pair<N, Map<E, N>>> = mappedEdges

}
