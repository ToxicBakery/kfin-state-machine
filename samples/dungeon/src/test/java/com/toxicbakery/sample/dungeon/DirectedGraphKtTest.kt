package com.toxicbakery.sample.dungeon

import com.toxicbakery.kfinstatemachine.TransitionDef
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.sample.dungeon.DirectedGraphKtTest.Action.Release
import com.toxicbakery.sample.dungeon.DirectedGraphKtTest.Action.Store
import com.toxicbakery.sample.dungeon.DirectedGraphKtTest.Energy.Kinetic
import com.toxicbakery.sample.dungeon.DirectedGraphKtTest.Energy.Potential
import org.junit.Test
import kotlin.reflect.KClass

class DirectedGraphKtTest {

    @Test
    fun getToTransitionRules() {
        DirectedGraph(
            mapOf(
                Potential to mapOf(Release::class to Kinetic),
                Kinetic to mapOf(Store::class to Potential)
            )
        )
            .toTransitionRules
            .also { transitions: List<TransitionDef<Energy, out KClass<out Action>>> ->
                transitions.first { it.oldState == Potential && it.newState == Kinetic }
                transitions.first { it.oldState == Kinetic && it.newState == Potential }
            }
    }

    enum class Energy {
        Potential,
        Kinetic
    }

    enum class Action {
        Release,
        Store
    }

}