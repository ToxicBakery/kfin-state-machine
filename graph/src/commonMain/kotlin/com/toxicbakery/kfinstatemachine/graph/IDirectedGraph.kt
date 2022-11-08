package com.toxicbakery.kfinstatemachine.graph

/**
 * Directed graph that defines nodes and edges.
 */
interface IDirectedGraph<N, E> {

    /**
     * All of the nodes defined in the graph.
     */
    val nodes: Set<N>

    /**
     * Edges defined by a node.
     */
    fun edges(
        node: N,
        defaultValue: () -> Map<E, N> = { throw Exception("Node not in graph.") }
    ): Map<E, N>

    /**
     * Transitions available to a specific node.
     */
    fun transitions(node: N): Set<E>

}