package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.BaseMachine
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.sample.dungeon.Direction.*
import com.toxicbakery.sample.dungeon.Direction.Companion.DIRECTIONS
import java.util.*

private val invalidPoint = Point(-1, -1)

fun mapToDirectedGraph(map: Array<Array<Boolean>>): BaseMachine<Point, Label> {
    map.map { it.size }
            .sorted()
            .also {
                if (it[0] != it.last() || it[0] != map.size)
                    throw Exception("Map must be square!")
            }

    return mutableMapOf<Point, MutableMap<Label, Point>>()
            .apply {
                for (y in 0 until map.size)
                    for (x in 0 until map.size)
                        putAll(createEdges(map, Point(x, y)))
            }
            .let { edges -> DirectedGraph(edges) }
            .let { directedGraph ->
                BaseMachine(
                        directedGraph = directedGraph,
                        initialState = Random()
                                .nextInt(directedGraph.nodes.size)
                                .let { directedGraph.nodes.elementAt(it) })
            }
}

private fun createEdges(
        map: Array<Array<Boolean>>,
        point: Point
): MutableMap<Point, MutableMap<Label, Point>> = mutableMapOf<Point, MutableMap<Label, Point>>()
        .apply {
            DIRECTIONS.forEach { direction: Direction ->
                when (direction) {
                    North -> point.copy(y = wrapPosition(map, point.y - 1))
                    South -> point.copy(y = wrapPosition(map, point.y + 1))
                    West -> point.copy(x = wrapPosition(map, point.x - 1))
                    East -> point.copy(x = wrapPosition(map, point.x + 1))
                }.also { target ->
                    if (target != invalidPoint
                            && targetIsValid(map, target)) {

                        val label = Label(direction.shortId)
                        getOrPut(point, { mutableMapOf() })[label] = target
                    }
                }
            }
        }

private fun targetIsValid(
        map: Array<Array<Boolean>>,
        point: Point
) = map[point.x][point.y]

private fun wrapPosition(map: Array<Array<Boolean>>, y: Int): Int =
        when {
            y < 0 -> map.size - 1
            y == map.size -> 0
            else -> y
        }
