package com.toxicbakery.kfinstatemachine.graph

import kotlin.test.Test
import kotlin.test.assertEquals

class MapPathsKtTest {

    @Test
    fun mapAllPaths() {
        mapOf(
                "node_1" to mapOf(
                        "edge_1" to "node_2",
                        "edge_2" to "node_3"
                ),
                "node_2" to mapOf(
                        "edge_3" to "node_3",
                        "edge_4" to "node_4"
                ),
                "node_4" to mapOf(
                        "edge_5" to "node_2"
                ))
                .let { edges -> DirectedGraph(edges) }
                .mapAcyclicPaths("node_1")
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