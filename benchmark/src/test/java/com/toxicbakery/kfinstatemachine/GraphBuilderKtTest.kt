package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.GraphNode
import org.junit.Assert.assertEquals
import org.junit.Test

class GraphBuilderKtTest {

    @Test
    fun graphEdgesTest() {
        assertEquals(
                setOf(
                        GraphEdge(
                                left = GraphNode("node_1"),
                                right = GraphNode("node_2"),
                                label = "edge_1"
                        ),
                        GraphEdge(
                                left = GraphNode("node_2"),
                                right = GraphNode("node_3"),
                                label = "edge_2"
                        )
                ),
                createGraphEdges(2)
        )
    }

}