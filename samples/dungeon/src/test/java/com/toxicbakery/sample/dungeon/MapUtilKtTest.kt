package com.toxicbakery.sample.dungeon

import org.junit.Test

class MapUtilKtTest {

    @Test
    fun mapToDirectedGraph() {
        mapToDirectedGraph(
                Array(2, { _ ->
                    Array(2, { _ -> true })
                })
        )
    }

    @Test(expected = Exception::class)
    fun mapToDirectedGraphNotSquare() {
        mapToDirectedGraph(
                Array(2, { _ ->
                    Array(1, { _ -> true })
                })
        )
    }

}