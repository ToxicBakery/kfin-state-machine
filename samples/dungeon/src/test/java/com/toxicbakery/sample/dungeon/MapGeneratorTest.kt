package com.toxicbakery.sample.dungeon

import org.junit.Test

class MapGeneratorTest {

    @Test
    fun generateMap() {
        MapGenerator().generateMap()
            .forEach {
                println(
                    it.joinToString(transform = { value ->
                        if (value) "1" else "0"
                    })
                )
            }
    }

}