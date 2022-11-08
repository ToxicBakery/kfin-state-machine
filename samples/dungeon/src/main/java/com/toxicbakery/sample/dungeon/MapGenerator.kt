package com.toxicbakery.sample.dungeon

import java.util.*

class MapGenerator(
    private val dimensions: Int = 20,
    private val tunnels: Int = 50,
    private val length: Int = 8
) {

    init {
        if (dimensions <= 1) throw Exception("Dimension must be greater than 1")
        if (tunnels < 1) throw Exception("Tunnels must be 1 or greater")
        if (length < 1) throw Exception("Length must be 1 or greater")
    }

    private val randomPoint: Int
        get() = Random().nextInt(dimensions)

    private val randomDirection: Array<Int>
        get() = Random().nextInt(directions.size)
            .let(directions::get)

    fun generateMap(): Array<Array<Boolean>> {
        val map = createInitialMap(false)
        var previousDirection: Array<Int> = randomDirection
        var currentDirection: Array<Int>

        var remainingTunnels = tunnels
        var currentRow = randomPoint
        var currentColumn = randomPoint

        while (
            dimensions > 0
            && length > 0
            && remainingTunnels > 0
        ) {

            // Pick a new direction to walk
            do {
                currentDirection = randomDirection
            } while (
                currentDirection[0] == -previousDirection[0] && currentDirection[1] == -previousDirection[1]
                || currentDirection[0] == previousDirection[0] && currentDirection[1] == previousDirection[1]
            )

            // Draw a tunnel on the map
            val targetLength = Math.ceil(Random().nextInt(length).toDouble()).toInt()
            var tunnelLength = 0
            while (tunnelLength < targetLength) {
                if (currentRow == 0 && currentDirection[0] == -1
                    || currentColumn == 0 && currentDirection[1] == -1
                    || currentRow == dimensions - 1 && currentDirection[0] == 1
                    || currentColumn == dimensions - 1 && currentDirection[1] == 1
                ) {
                    break
                } else {
                    map[currentRow][currentColumn] = true
                    currentRow += currentDirection[0]
                    currentColumn += currentDirection[1]
                    ++tunnelLength
                }
            }

            // Update for next pass if a tunnel was drawn
            if (tunnelLength > 0) {
                previousDirection = currentDirection
                --remainingTunnels
            }
        }

        return map
    }

    /**
     * Generate the initial map which is a 2d array filled with the given value.
     */
    private fun createInitialMap(value: Boolean): Array<Array<Boolean>> =
        Array(dimensions, { _ ->
            Array(dimensions, { _ -> value })
        })

    companion object {
        private val directions = arrayOf(
            arrayOf(-1, 0),
            arrayOf(0, -1),
            arrayOf(0, 1),
            arrayOf(1, 0)
        )
    }

}