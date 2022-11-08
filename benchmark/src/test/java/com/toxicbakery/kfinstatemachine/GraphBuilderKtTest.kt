package com.toxicbakery.kfinstatemachine

import org.junit.Assert.assertEquals
import org.junit.Test

class GraphBuilderKtTest {

    @Test
    fun graphEdgesTest() {
        assertEquals(
            mapOf(
                TestFiniteState("node_1") to mapOf(TestTransition("edge_1") to TestFiniteState("node_2")),
                TestFiniteState("node_2") to mapOf(TestTransition("edge_2") to TestFiniteState("node_3"))
            ),
            createGraphEdges(2)
        )
    }

}