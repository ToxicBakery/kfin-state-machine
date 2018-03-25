package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.FiniteState

data class Point(
        val x: Int,
        val y: Int
) : FiniteState {

    override val id: String
        get() = "$x,$y"

}