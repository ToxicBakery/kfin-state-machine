package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.sample.dungeon.Direction.*
import com.toxicbakery.sample.dungeon.Direction.Companion.DIRECTIONS

fun mapToDirectedGraph(map: Array<Array<Boolean>>): DirectedGraph<Point, Direction> =
    map.map { it.size }
        .sorted()
        .also {
            if (it[0] != it.last() || it[0] != map.size)
                throw Exception("Map must be square!")
        }
        .let { mutableMapOf<Point, MutableMap<Direction, Point>>() }
        .apply {
            for (y in map.indices)
                for (x in map.indices)
                    putAll(createEdges(map, Point(x, y)))
        }
        .let { edges: MutableMap<Point, MutableMap<Direction, Point>> ->
            DirectedGraph(edges)
        }

private fun createEdges(
    map: Array<Array<Boolean>>,
    point: Point
): MutableMap<Point, MutableMap<Direction, Point>> =
    mutableMapOf<Point, MutableMap<Direction, Point>>()
        .also { parentMap ->
            DIRECTIONS.forEach { direction: Direction ->
                when (direction) {
                    North -> point.copy(y = indexWrap(map.size, point.y - 1))
                    South -> point.copy(y = indexWrap(map.size, point.y + 1))
                    West -> point.copy(x = indexWrap(map.size, point.x - 1))
                    East -> point.copy(x = indexWrap(map.size, point.x + 1))
                }.let { target ->
                    if (targetIsValid(map, target)) {
                        direction.also { label ->
                            parentMap.getOrPut(
                                key = point,
                                defaultValue = { mutableMapOf() }
                            )[label] = target
                        }
                    }
                }
            }
        }

private fun targetIsValid(
    map: Array<Array<Boolean>>,
    point: Point
) = map[point.x][point.y]

/**
 * Wrap an integer to a value between zero and an exclusive max assuming the
 * integer will only ever be at most one less than zero or equal to the size.
 */
private fun indexWrap(size: Int, position: Int): Int =
    when {
        position < 0 -> size - 1
        position == size -> 0
        else -> position
    }
