package com.toxicbakery.kfinstatemachine.graph

import org.junit.Assert.*
import org.junit.Test

class DirectedGraphTest {

    @Test
    fun rightEdgesForValue() {
        val edges = setOf(
                GraphEdge(
                        left = "node_1",
                        right = "node_2",
                        label = "edge_1"
                ))

        DirectedGraph(edges)
                .exitingEdgesForNodeValue("node_1")
                .let { assertEquals(edges, it) }
    }

    @Test
    fun ambiguousNodes() {
        val edges = setOf(
                GraphEdge(
                        left = "node_1",
                        right = "node_2",
                        label = "edge_1"
                ),
                GraphEdge(
                        left = "node_1",
                        right = "node_3",
                        label = "edge_1"
                ))

        try {
            DirectedGraph(edges)
            fail("Expected exception for ambiguous edges.")
        } catch (e: Exception) {
            assertTrue(e.message!!.startsWith("Ambiguous edges detected for "))
        }
    }

}