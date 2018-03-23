package com.toxicbakery.kfinstatemachine.graph

import org.junit.Assert.assertEquals
import org.junit.Test

class MapPathsKtTest {

    @Test
    fun mapAllPaths() {
        val edges = setOf(
                GraphEdge(
                        left = GraphNode("node_1"),
                        right = GraphNode("node_2"),
                        label = "edge_1"
                ),
                GraphEdge(
                        left = GraphNode("node_1"),
                        right = GraphNode("node_3"),
                        label = "edge_2"
                ),
                GraphEdge(
                        left = GraphNode("node_2"),
                        right = GraphNode("node_3"),
                        label = "edge_3"
                ),
                GraphEdge(
                        left = GraphNode("node_2"),
                        right = GraphNode("node_4"),
                        label = "edge_4"
                ),
                GraphEdge(
                        left = GraphNode("node_4"),
                        right = GraphNode("node_2"),
                        label = "edge_5"
                ))

        val directedGraph = DirectedGraph(edges)
        directedGraph.mapAcyclicPaths(GraphNode("node_1"))
                .let { foundPaths: Set<List<GraphNode<String>>> ->
                    assertEquals(
                            setOf(
                                    listOf(
                                            GraphNode("node_1"),
                                            GraphNode("node_2"),
                                            GraphNode("node_3")
                                    ),
                                    listOf(
                                            GraphNode("node_1"),
                                            GraphNode("node_2"),
                                            GraphNode("node_4")
                                    ),
                                    listOf(
                                            GraphNode("node_1"),
                                            GraphNode("node_3")
                                    )
                            ),
                            foundPaths
                    )
                }
    }

}