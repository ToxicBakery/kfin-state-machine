package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.StateMachine
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import java.util.*

class MapMachine private constructor(
        directedGraph: DirectedGraph<Point, Label>,
        initialState: Point
) : StateMachine<Point, Label>(
        directedGraph,
        initialState
) {

    companion object {

        private fun generateMap(size: Int) = MapGenerator(dimensions = size)
                .generateMap()

        private fun randomStartingPoint(directedGraph: DirectedGraph<Point, Label>) =
                Random()
                        .nextInt(directedGraph.nodes.size)
                        .let { directedGraph.nodes.elementAt(it) }

        fun createNewMachine(mapSize: Int): MapMachine =
                mapToDirectedGraph(generateMap(mapSize))
                        .let { directedGraph ->
                            MapMachine(
                                    directedGraph,
                                    randomStartingPoint(directedGraph))
                        }
    }
}