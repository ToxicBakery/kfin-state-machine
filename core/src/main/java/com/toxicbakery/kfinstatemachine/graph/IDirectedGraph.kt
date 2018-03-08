package com.toxicbakery.kfinstatemachine.graph

interface IDirectedGraph<N, out E> {
    val nodes: Set<GraphNode<N>>
    fun shortestPath(
            source: GraphNode<N>,
            destination: GraphNode<N>
    ): List<GraphNode<N>>

    fun rightEdgesForValue(nodeValue: N): Set<GraphEdge<N, E>>
}