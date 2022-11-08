package com.toxicbakery.sample.dungeon

import java.io.PrintStream

fun main() {
    Application().apply {
        System.console()?.apply {
            while (true) if (gameLoopProcessor(readLine())) break
        }
    }
}

class Application(
    private val outputPrintStream: PrintStream = System.out
) {

    private val machine: MapMachine = MapMachine.createNewMachine(mapSize = MAP_SIZE)

    private val exploration = Exploration(MAP_SIZE, VIEWABLE_SIZE)
        .apply { addExploredPoint(machine.state) }

    private val allDirections = Direction.DIRECTIONS.joinToString { it.shortId }

    init {
        look()
    }

    /**
     * Attempt to execute a given command against the game.
     * @return true if the game should exit
     */
    fun gameLoopProcessor(command: String): Boolean {
        when {
            command == "exit" -> return true
            command.startsWith("walk") -> walk(command.removePrefix("walk").trim())
            else -> help()
        }

        println(exploration.printMap(machine.state))
        look()
        println()
        return false
    }

    private fun walk(direction: String) {
        try {
            Direction.fromString(direction.substring(0, 1))
                .let(Direction::shortId)
                .also(::doWalk)
        } catch (e: Exception) {
            println("Invalid direction, expecting $allDirections")
        }
    }

    private fun doWalk(direction: String) {
        try {
            machine.transition(Direction.fromString(direction))
            exploration.addExploredPoint(machine.state)
            println("You walk to the $direction")
        } catch (e: Exception) {
            println("Invalid direction $direction")
            machine.availableDirections().also { availableDirections ->
                if (availableDirections.isNotEmpty())
                    println("Valid directions ${availableDirections.joinToString(transform = Direction::shortId)}")
            }
        }
    }

    private fun look() {
        machine.availableDirections()
            .also { availableDirections ->
                when {
                    availableDirections.size == 1 -> println(
                        """You look around and realize you are at a paths end.
                                | A path runs ${availableDirections.first().shortId}""".trimMargin()
                    )

                    else -> println("You look around and see paths to the ${availableDirections.joinToString(transform = Direction::shortId)}")
                }
            }
    }

    private fun help() {
        println(
            """
                Available Commands:
                walk [$allDirections] - Walk in a cardinal direction
                help
                exit
                """.trimIndent()
        )
    }

    private fun println(message: String = "") = outputPrintStream.println(message)

    companion object {
        private const val MAP_SIZE: Int = 20
        private const val VIEWABLE_SIZE: Int = 7
    }

}

