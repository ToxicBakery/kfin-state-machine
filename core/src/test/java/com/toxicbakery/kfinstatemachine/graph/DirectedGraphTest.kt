package com.toxicbakery.kfinstatemachine.graph

import org.junit.Assert.assertEquals
import org.junit.Test

class DirectedGraphTest {

    @Test
    fun rightEdgesForValue() {
        val edges = setOf(
                GraphEdge(
                        left = GraphNode("node_1"),
                        right = GraphNode("node_2"),
                        label = "edge_1"
                ))

        DirectedGraph(edges)
                .rightEdgesForValue("node_1")
                .let { assertEquals(edges, it) }
    }
}