package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.BaseMachine
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.GraphEdge
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

    val edges = mutableSetOf<GraphEdge<Point, Label>>()
    for (y in 0 until map.size)
        for (x in 0 until map.size)
            edges.addAll(createEdges(map, Point(x, y)))

    val graph = DirectedGraph(edges)
    return BaseMachine(
            directedGraph = graph,
            initialState = Random()
                    .nextInt(edges.size)
                    .let(edges::elementAt)
                    .left
    )
}

private fun createEdges(
        map: Array<Array<Boolean>>,
        point: Point
): MutableSet<GraphEdge<Point, Label>> = mutableSetOf<GraphEdge<Point, Label>>()
        .also { edges ->
            DIRECTIONS.forEach { direction: Direction ->
                when (direction) {
                    North -> point.copy(y = wrapPosition(map, point.y - 1))
                    South -> point.copy(y = wrapPosition(map, point.y + 1))
                    West -> point.copy(x = wrapPosition(map, point.x - 1))
                    East -> point.copy(x = wrapPosition(map, point.x + 1))
                }.also { target ->
                    if (target != invalidPoint
                            && targetIsValid(map, target))
                        edges.add(createEdge(point, target, direction))
                }
            }
        }

private fun targetIsValid(
        map: Array<Array<Boolean>>,
        point: Point
) = map[point.x][point.y]

private fun createEdge(
        start: Point,
        destination: Point,
        direction: Direction
): GraphEdge<Point, Label> =
        GraphEdge(
                left = start,
                right = destination,
                label = Label(direction.shortId)
        )

private fun wrapPosition(map: Array<Array<Boolean>>, y: Int): Int =
        when {
            y < 0 -> map.size - 1
            y == map.size -> 0
            else -> y
        }
