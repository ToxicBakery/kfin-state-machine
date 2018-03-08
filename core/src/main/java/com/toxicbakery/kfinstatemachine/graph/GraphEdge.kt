package com.toxicbakery.kfinstatemachine.graph

data class GraphEdge<out V, out L>(
        val left: GraphNode<V>,
        val right: GraphNode<V>,
        val label: L
)