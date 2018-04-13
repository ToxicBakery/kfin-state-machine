package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.Energy.*
import com.toxicbakery.kfinstatemachine.EnergyTransition.*
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.Semaphore
import kotlin.reflect.KClass

class StateMachineTest {

    private val directedGraph: IDirectedGraph<Energy, KClass<*>> = DirectedGraph(
            mappedEdges = mapOf(
                    Potential to mapOf<KClass<*>, Energy>(Release::class to Kinetic),
                    Kinetic to mapOf<KClass<*>, Energy>(Store::class to Potential))
    )

    @Test
    fun performTransition() {
        val machine = StateMachine(
                directedGraph = DirectedGraph(
                        mapOf(
                                Potential to mapOf<KClass<*>, Energy>(Release::class to Kinetic),
                                Kinetic to mapOf<KClass<*>, Energy>(Store::class to Potential)
                        )
                ),
                initialState = Potential)

        machine.performTransition(Release)
        assertEquals(Kinetic, machine.state)
    }

    @Test(expected = Exception::class)
    fun findNextNode() {
        val machine = StateMachine(
                directedGraph = directedGraph,
                initialState = Potential)

        machine.performTransition(InvalidTransition)
    }

    @Test
    fun onTransitionListener() {
        val machine = StateMachine(
                directedGraph = directedGraph,
                initialState = Potential)

        val semaphore = Semaphore(0)
        val listener = machine.addOnTransitionListener { _ -> semaphore.release() }
        machine.performTransition(Release)
        assertTrue(semaphore.tryAcquire())

        machine.removeOnTransitionListener(listener)
        assertFalse(semaphore.tryAcquire())
    }

    @Test
    fun onStateChangeListener() {
        val machine = StateMachine(
                directedGraph = directedGraph,
                initialState = Potential)

        val semaphore = Semaphore(0)
        val listener = machine.addOnStateChangeListener { _ -> semaphore.release() }
        machine.performTransition(Release)
        assertTrue(semaphore.tryAcquire())

        machine.removeOnStateChangedListener(listener)
        assertFalse(semaphore.tryAcquire())
    }

    @Test
    fun transitionEvent() {
        val machine = StateMachine(
                directedGraph = directedGraph,
                initialState = Potential)

        machine.addOnTransitionListener(
                { transitionEvent ->
                    assertEquals(TransitionEvent(Release, Kinetic), transitionEvent)
                    assertEquals(Release, transitionEvent.transition)
                    assertEquals(Kinetic, transitionEvent.targetState)
                })
                .also { machine.performTransition(Release) }
                .also(machine::removeOnTransitionListener)
    }

    @Test
    fun availableTransitions() {
        val machine = StateMachine(
                directedGraph = directedGraph,
                initialState = Potential)

        assertEquals(
                setOf(Release::class),
                machine.availableTransitions)
    }

    @Test(expected = Exception::class)
    fun initialStateOutsideGraph() {
        StateMachine(
                directedGraph = directedGraph,
                initialState = InvalidState)
    }

    @Test
    fun transitionsForTargetState() {
        val machine = StateMachine(
                directedGraph = directedGraph,
                initialState = Potential)

        assertEquals(setOf<KClass<*>>(), machine.transitionsForTargetState(Potential))
        assertEquals(setOf<KClass<*>>(Release::class), machine.transitionsForTargetState(Kinetic))
    }

}