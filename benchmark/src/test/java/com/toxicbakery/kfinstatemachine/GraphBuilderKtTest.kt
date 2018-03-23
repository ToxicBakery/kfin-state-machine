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
                                left = GraphNode(TestFiniteState("node_1")),
                                right = GraphNode(TestFiniteState("node_2")),
                                label = TestTransition("edge_1")
                        ),
                        GraphEdge(
                                left = GraphNode(TestFiniteState("node_2")),
                                right = GraphNode(TestFiniteState("node_3")),
                                label = TestTransition("edge_2")
                        )
                ),
                createGraphEdges(2)
        )
    }

}