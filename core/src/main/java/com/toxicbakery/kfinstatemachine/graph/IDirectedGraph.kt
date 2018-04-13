package com.toxicbakery.kfinstatemachine.graph

/**
 * Directed graph that defines nodes and edges.
 */
interface IDirectedGraph<N, L> {

    /**
     * All of the nodes defined in the graph.
     */
    val nodes: Set<N>

    /**
     * Edges defined by a node.
     */
    fun nodeEdges(
            node: N,
            defaultValue: () -> Map<L, N> = { throw Exception("Node not in graph.") }
    ): Map<L, N>

    /**
     * Transitions available to a specific node.
     */
    fun nodeTransitions(node: N): Set<L>

}