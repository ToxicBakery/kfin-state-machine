package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.BaseMachine
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import java.util.*

private val directions = arrayOf("n", "s", "w", "e")
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
            directions.forEach { direction ->
                when (direction) {
                    "n" -> point.copy(y = wrapPosition(map, point.y - 1))
                    "s" -> point.copy(y = wrapPosition(map, point.y + 1))
                    "w" -> point.copy(x = wrapPosition(map, point.x - 1))
                    "e" -> point.copy(x = wrapPosition(map, point.x + 1))
                    else -> invalidPoint
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
        direction: String
): GraphEdge<Point, Label> =
        GraphEdge(
                left = start,
                right = destination,
                label = Label(direction)
        )

private fun wrapPosition(map: Array<Array<Boolean>>, y: Int): Int =
        when {
            y < 0 -> map.size - 1
            y == map.size -> 0
            else -> y
        }
