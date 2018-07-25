package com.toxicbakery.kfinstatemachine.graph

import com.toxicbakery.kfinstatemachine.TransitionRule
import com.toxicbakery.kfinstatemachine.graph.DirectedGraphKtTest.Action.Release
import com.toxicbakery.kfinstatemachine.graph.DirectedGraphKtTest.Action.Store
import com.toxicbakery.kfinstatemachine.graph.DirectedGraphKtTest.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.graph.DirectedGraphKtTest.Energy.Potential
import org.junit.Test
import kotlin.reflect.KClass

class DirectedGraphKtTest {

    @Test
    fun getToTransitionRules() {
        DirectedGraph(
                mapOf(
                        Potential to mapOf(Release::class to Kinetic),
                        Kinetic to mapOf(Store::class to Potential)
                ))
                .toTransitionRules
                .also { transitions: List<TransitionRule<Energy, out KClass<out Action>>> ->
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