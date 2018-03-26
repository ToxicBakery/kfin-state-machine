package com.toxicbakery.kfinstatemachine.graph

data class GraphEdge<out N, out L>(
        val left: N,
        val right: N,
        val label: L
)