package com.toxicbakery.sample.dungeon

sealed class Direction(val shortId: String) {
    object North : Direction("n")
    object South : Direction("s")
    object West : Direction("w")
    object East : Direction("e")

    companion object {
        val DIRECTIONS: List<Direction> = listOf(
                North,
                South,
                West,
                East)

        fun fromString(direction: String): Direction =
                when (direction) {
                    North.shortId -> North
                    South.shortId -> South
                    West.shortId -> West
                    East.shortId -> East
                    else -> throw Exception("Invalid direction $direction")
                }
    }
}