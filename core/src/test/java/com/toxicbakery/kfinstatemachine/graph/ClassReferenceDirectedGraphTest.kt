package com.toxicbakery.kfinstatemachine.graph

import com.toxicbakery.kfinstatemachine.Energy
import com.toxicbakery.kfinstatemachine.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.Energy.Potential
import com.toxicbakery.kfinstatemachine.EnergyTransition
import com.toxicbakery.kfinstatemachine.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.EnergyTransition.Store
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

class ClassReferenceDirectedGraphTest {

    private val graph
        get() = ClassReferenceDirectedGraph.newInstance<KClass<Energy>, KClass<EnergyTransition>>(
                mapOf(
                        Potential::class to mapOf(Release::class to Kinetic::class),
                        Kinetic::class to mapOf(Store::class to Potential::class)
                ))

    @Test
    fun nodes() {
        assertEquals(
                setOf(Potential::class, Kinetic::class),
                graph.nodes)
    }

    @Test
    fun nodeTransitions() {
        assertEquals(
                setOf(Release::class),
                graph.nodeTransitions(Potential::class.cast()))
    }

    @Test
    fun nodeEdges() {
        assertEquals(
                mapOf(Release::class to Kinetic::class),
                graph.nodeEdges(Potential::class.cast()))
    }

}