package com.toxicbakery.kfinstatemachine.graph

/**
 * Directed graph that defines nodes and edges.
 */
interface IDirectedGraph<N, out L> {

    /**
     * All of the nodes defined in the graph.
     */
    val nodes: Set<N>

    /**
     * All of the edges defined in the graph.
     */
    val edges: Set<GraphEdge<N, L>>

    /**
     * Edges of the graph grouped by their left node.
     */
    val mappedEdges: Map<N, List<GraphEdge<N, L>>>

    /**
     * Transitions available to a specific node.
     */
    fun nodeTransitions(node: N): Set<L>

}