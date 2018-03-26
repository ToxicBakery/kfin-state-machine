package com.toxicbakery.kfinstatemachine.graph

interface IDirectedGraph<N, out L> {
    val nodes: Set<N>

    val edges: Set<GraphEdge<N, L>>

    val mappedEdges: Map<N, List<GraphEdge<N, L>>>

    fun nodeTransitions(node: N): Set<L>

}