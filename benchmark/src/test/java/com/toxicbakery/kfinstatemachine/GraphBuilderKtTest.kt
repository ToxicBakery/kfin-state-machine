package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import org.junit.Assert.assertEquals
import org.junit.Test

class GraphBuilderKtTest {

    @Test
    fun graphEdgesTest() {
        assertEquals(
                setOf(
                        GraphEdge(
                                left = TestFiniteState("node_1"),
                                right = TestFiniteState("node_2"),
                                label = TestTransition("edge_1")
                        ),
                        GraphEdge(
                                left = TestFiniteState("node_2"),
                                right = TestFiniteState("node_3"),
                                label = TestTransition("edge_2")
                        )
                ),
                createGraphEdges(2)
        )
    }

}