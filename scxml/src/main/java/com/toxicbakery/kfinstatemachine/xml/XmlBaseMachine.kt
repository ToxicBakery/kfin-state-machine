package com.toxicbakery.kfinstatemachine.xml

import com.toxicbakery.kfinstatemachine.BaseMachine
import com.toxicbakery.kfinstatemachine.FiniteState
import com.toxicbakery.kfinstatemachine.Transition
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph

data class XmlBaseMachine(
        private val directedGraph: DirectedGraph< FiniteState, Transition>,
        private val initialState: FiniteState
) : BaseMachine<FiniteState, Transition>(directedGraph, initialState)