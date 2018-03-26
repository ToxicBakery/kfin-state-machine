package com.toxicbakery.sample.dungeon

import org.junit.Test

class ExplorationTest {

    @Test
    fun printMap() {
        val exploration = Exploration(20, 7)
        exploration.addExploredPoint(Point(1, 0))
        exploration.addExploredPoint(Point(2, 0))
        exploration.addExploredPoint(Point(2, 1))
        exploration.addExploredPoint(Point(2, 19))

        println(exploration.printMap(Point(2, 19)))
    }

}