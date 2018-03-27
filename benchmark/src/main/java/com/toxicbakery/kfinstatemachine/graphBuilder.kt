package com.toxicbakery.kfinstatemachine

fun createGraphEdges(
        edgeCount: Int
): Map<TestFiniteState, Map<TestTransition, TestFiniteState>> =
        mutableMapOf<TestFiniteState, MutableMap<TestTransition, TestFiniteState>>()
                .apply {
                    for (i in 1..edgeCount) {
                        val left = TestFiniteState("node_$i")
                        val right = TestFiniteState("node_${i + 1}")
                        val label = TestTransition("edge_$i")
                        getOrPut(left, { mutableMapOf() })[label] = right
                    }
                }