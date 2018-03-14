package com.toxicbakery.kfinstatemachine.graph

interface IDirectedGraph<out N, out E> {
    val nodes: Set<GraphNode<N>>

    val edges: Set<GraphEdge<N, E>>

}