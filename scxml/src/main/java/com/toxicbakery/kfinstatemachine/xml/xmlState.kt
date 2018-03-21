package com.toxicbakery.kfinstatemachine.xml

import com.toxicbakery.kfinstatemachine.BaseMachine
import com.toxicbakery.kfinstatemachine.FiniteState
import com.toxicbakery.kfinstatemachine.StateMachine
import com.toxicbakery.kfinstatemachine.Transition
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.kfinstatemachine.graph.GraphEdge
import com.toxicbakery.kfinstatemachine.graph.GraphNode
import com.toxicbakery.kfinstatemachine.xml.model.XmlRoot
import com.toxicbakery.kfinstatemachine.xml.model.XmlState
import com.toxicbakery.kfinstatemachine.xml.model.XmlTransition

/**
 * Create a simple machine from this root node.
 */
fun XmlRoot.createSimpleMachine() = states
                .flatMap { xmlState ->
                    xmlState.transitions.map { xmlTransition ->
                        GraphEdge(
                                left = GraphNode(xmlState.toFinite),
                                right = GraphNode(xmlTransition.targetState),
                                label = FiniteXmlTransition(xmlTransition.event)
                        )
                    }
                }
                .toSet()
                .let { edges: Set<GraphEdge<FiniteState, Transition>> ->
                    XmlBaseMachine(
                            directedGraph = DirectedGraph(edges),
                            initialState = FiniteXmlState(initial)
                    )
                }

/**
 * Create a parallel machine from this root node and all sub machines.
 */
fun XmlRoot.createParallelMachines() =
        mutableMapOf<String, StateMachine<FiniteState, Transition>>()
                .also { namedMachines -> createParallelMachines(namedMachines, parallel) }

internal fun createParallelMachines(
        namedMachines: MutableMap<String, StateMachine<FiniteState, Transition>>,
        parallel: List<XmlState>
) = parallel
        .flatMap(XmlState::states)
        .forEach { xmlState -> xmlState.createParallelMachine(namedMachines) }

private fun XmlState.createParallelMachine(
        namedMachines: MutableMap<String, StateMachine<FiniteState, Transition>>
) {
    states.forEach { }
    namedMachines[id] = BaseMachine(
            directedGraph = DirectedGraph(setOf()),
            initialState = FiniteXmlState("")
    )
}

val XmlState.toFinite: FiniteXmlState
    get() = FiniteXmlState(id)

val XmlTransition.toFinite: FiniteXmlTransition
    get() = FiniteXmlTransition(event)

val XmlTransition.targetState: FiniteXmlState
    get() = FiniteXmlState(target)
