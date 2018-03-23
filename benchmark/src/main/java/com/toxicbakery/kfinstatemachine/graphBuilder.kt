package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.GraphNode

fun createGraphEdges(
        edgeCount: Int
): Set<GraphEdge<TestFiniteState, TestTransition>> =
        mutableSetOf<GraphEdge<TestFiniteState, TestTransition>>()
                .apply {
                    for (i in 1..edgeCount) {
                        add(
                                GraphEdge(
                                        left = GraphNode(TestFiniteState("node_$i")),
                                        right = GraphNode(TestFiniteState("node_${i + 1}")),
                                        label = TestTransition("edge_$i")
                                )
                        )
                    }
                }
                .toSet()