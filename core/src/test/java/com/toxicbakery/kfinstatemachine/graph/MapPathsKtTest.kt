package com.toxicbakery.kfinstatemachine.graph

import org.junit.Assert.assertEquals
import org.junit.Test

class MapPathsKtTest {

    @Test
    fun mapAllPaths() {
        val edges = setOf(
                GraphEdge(
                        left = "node_1",
                        right = "node_2",
                        label = "edge_1"
                ),
                GraphEdge(
                        left = "node_1",
                        right = "node_3",
                        label = "edge_2"
                ),
                GraphEdge(
                        left = "node_2",
                        right = "node_3",
                        label = "edge_3"
                ),
                GraphEdge(
                        left = "node_2",
                        right = "node_4",
                        label = "edge_4"
                ),
                GraphEdge(
                        left = "node_4",
                        right = "node_2",
                        label = "edge_5"
                ))

        val directedGraph = DirectedGraph(edges)
        directedGraph.mapAcyclicPaths("node_1")
                .let { foundPaths: Set<List<String>> ->
                    assertEquals(
                            setOf(
                                    listOf(
                                            "node_1",
                                            "node_2",
                                            "node_3"
                                    ),
                                    listOf(
                                            "node_1",
                                            "node_2",
                                            "node_4"
                                    ),
                                    listOf(
                                            "node_1",
                                            "node_3"
                                    )
                            ),
                            foundPaths
                    )
                }
    }

}