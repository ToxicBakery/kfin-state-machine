package com.toxicbakery.kfinstatemachine.graph

import org.junit.Assert
import org.junit.Test

class DijkstraAlgorithmTest {

    @Test
    fun shortestPathTo() {
        val nodes = listOf(
                GraphNode("node_1"),
                GraphNode("node_2"),
                GraphNode("node_3"),
                GraphNode("node_4"),
                GraphNode("node_5")
        )

        val edges = listOf(
                GraphEdge(nodes[0], nodes[1], "1 -> 2"),
                GraphEdge(nodes[1], nodes[2], "2 -> 3"),
                GraphEdge(nodes[2], nodes[3], "3 -> 4"),
                GraphEdge(nodes[3], nodes[4], "4 -> 5")
        )

        // Each graph node only has one edge sequentially from node 1 to node 5. Shortest path from nodes 1 to 5 should
        // return the entire graph.
        DirectedGraph(edges.toSet())
                .shortestPath(nodes[0], nodes[4])
                .also { calculated ->
                    Assert.assertEquals(
                            nodes,
                            calculated
                    )
                }

        // Retest the graph with a shortcut added from "node_2" to "node_5"
        edges.plus(GraphEdge(nodes[1], nodes[4], "2 -> 5"))
                .toSet()
                .let { DirectedGraph(it) }
                .shortestPath(nodes[0], nodes[4])
                .also { calculated ->
                    Assert.assertEquals(
                            listOf(
                                    GraphNode("node_1"),
                                    GraphNode("node_2"),
                                    GraphNode("node_5")
                            ),
                            calculated
                    )
                }
    }

    @Test(expected = Exception::class)
    fun shortestPathToUnrelatedNode() {
        DirectedGraph(setOf<GraphEdge<String, String>>())
                .shortestPath(GraphNode("node_1"), GraphNode("node_2"))
    }

}