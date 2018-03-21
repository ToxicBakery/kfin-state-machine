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
 * Create the machine(s) for this state and any sub states.
 */
fun XmlRoot.createRootMachine() =
        mutableMapOf<String, StateMachine<FiniteState, Transition>>()
                .also { namedMachines ->
                    when {
                        parallel != null -> createParallelMachines(namedMachines, parallel)
                        states.isNotEmpty() -> {
                            states
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
                                        namedMachines["root"] = XmlBaseMachine(
                                                directedGraph = DirectedGraph(edges),
                                                initialState = FiniteXmlState(initial)
                                        )
                                    }
                        }
                    }
                }

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
