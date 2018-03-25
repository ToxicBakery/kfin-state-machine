package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.StateMachine

fun main(args: Array<String>) {
    val application = Application()
    System.console()
            ?.apply {
                while (true) {
                    if (!application.gameLoopProcessor(readLine())) break
                }
            }
}

class Application {

    private val machine: StateMachine<Point, Label> =
            MapGenerator(dimensions = MAP_SIZE)
                    .generateMap()
                    .let(::mapToDirectedGraph)

    private val availableDirections: List<String>
        get() = machine.availableTransitions
                .map(Label::event)

    private val exploration = Exploration(MAP_SIZE, VIEWABLE_SIZE)
            .apply { addExploredPoint(machine.state) }

    init {
        look()
    }

    fun gameLoopProcessor(command: String): Boolean {
        when {
            command == "exit" -> return false
            command.startsWith("walk") -> walk(command.removePrefix("walk").trim())
            else -> help()
        }

        exploration.printMap(machine.state)
        look()
        println()
        return true
    }

    private fun walk(direction: String) {
        when (direction) {
            "n", "s", "e", "w" -> doWalk(direction)
            else -> println("Invalid direction, expecting n, s, e, or w")
        }
    }

    private fun doWalk(direction: String) {
        try {
            machine.performTransitionByName(direction)
            exploration.addExploredPoint(machine.state)
            println("You walk to the $direction")
        } catch (e: Exception) {
            println("Invalid direction $direction")
            availableDirections.also { availableDirections ->
                if (availableDirections.isNotEmpty())
                    println("Valid directions ${availableDirections.joinToString()}")
            }
        }
    }

    private fun look() {
        availableDirections
                .also { availableDirections ->
                    when {
                        availableDirections.size == 1 -> println("""You look around and realize you are at a paths end.
                                | A path runs ${availableDirections[0]}""".trimMargin())
                        else -> println("You look around and see paths to the ${availableDirections.joinToString()}")
                    }
                }
    }

    private fun help() {
        println("""
                Available Commands:
                walk [n,s,e,w] - Walk in a cardinal direction
                help
                exit
                """.trimIndent())
    }

    companion object {
        private const val MAP_SIZE: Int = 20
        private const val VIEWABLE_SIZE: Int = 7
    }

}

