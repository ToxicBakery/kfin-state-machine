package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.StateMachine
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import java.util.*

class MapMachine private constructor(
        private val directedGraph: DirectedGraph<Point, Direction>,
        initialState: Point
) : StateMachine<Point>(
        initialState,
        *graphToTransitions(directedGraph)
) {

    /**
     * Paths available from the current map location
     */
    fun availableDirections(): Set<Direction> = directedGraph
            .edges(state)
            .keys

    companion object {

        private fun generateMap(size: Int) = MapGenerator(dimensions = size)
                .generateMap()

        private fun randomStartingPoint(directedGraph: DirectedGraph<Point, Direction>) =
                Random()
                        .nextInt(directedGraph.nodes.size)
                        .let { directedGraph.nodes.elementAt(it) }

        private fun graphToTransitions(directedGraph: DirectedGraph<Point, Direction>) =
                directedGraph.nodes
                        .flatMap { leftNode ->
                            directedGraph.edges(leftNode)
                                    .map { (edge, rightNode) -> transition(leftNode, edge::class, rightNode) }
                        }
                        .toTypedArray()

        fun createNewMachine(mapSize: Int): MapMachine =
                mapToDirectedGraph(generateMap(mapSize))
                        .let { directedGraph ->
                            MapMachine(
                                    directedGraph,
                                    randomStartingPoint(directedGraph))
                        }
    }
}