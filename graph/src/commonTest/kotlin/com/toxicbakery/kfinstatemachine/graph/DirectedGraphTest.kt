package com.toxicbakery.kfinstatemachine.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DirectedGraphTest {

    @Test
    fun nodes() {
        DirectedGraph(listOf("node_1" to mapOf("edge_1" to "node_2")))
            .nodes
            .also { nodes -> assertEquals(setOf("node_1", "node_2"), nodes) }
    }

    @Test
    fun nodeTransitions() {
        DirectedGraph(listOf("node_1" to mapOf("edge_1" to "node_2")))
            .transitions("node_1")
            .also { transitions -> assertEquals(setOf("edge_1"), transitions) }
    }

    @Test
    fun nodeEdges() {
        DirectedGraph(listOf("node_1" to mapOf("edge_1" to "node_2")))
            .edges("node_1")
            .entries
            .first()
            .also { entry: Map.Entry<String, String> ->
                assertEquals("edge_1", entry.key)
                assertEquals("node_2", entry.value)
            }
    }

    @Test
    fun nodeNotInGraph() {
        try {
            DirectedGraph(listOf("node_1" to mapOf("edge_1" to "node_2")))
                .edges("node_3")
            fail("Expected Exception")
        } catch (e: Exception) {
        }
    }

    @Test
    fun nodeNotInGraph_withDefault() {
        DirectedGraph(listOf("node_1" to mapOf("edge_1" to "node_2")))
            .edges("node_3", { mapOf("edge_3" to "node_1") })
            .also {
                assertEquals(
                    mapOf("edge_3" to "node_1"),
                    it
                )
            }
    }

    @Test
    fun graph() {
        val graph = listOf("node_1" to mapOf("edge_1" to "node_2"))
        assertEquals(
            graph,
            DirectedGraph(graph).graph()
        )
    }

}