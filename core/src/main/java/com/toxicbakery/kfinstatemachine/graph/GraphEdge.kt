package com.toxicbakery.kfinstatemachine.graph

/**
 * Edge of a graph that defines both it's left and right nodes and a label describing the edge.
 *
 * @param left starting point of the edge
 * @param right ending point of the edge
 * @param label name of the edge between the two points
 */
data class GraphEdge<out N, out L>(
        val left: N,
        val right: N,
        val label: L
)