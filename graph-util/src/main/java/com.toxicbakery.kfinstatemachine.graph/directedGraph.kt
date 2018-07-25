package com.toxicbakery.kfinstatemachine.graph

import com.toxicbakery.kfinstatemachine.StateMachine.Companion.transition
import com.toxicbakery.kfinstatemachine.TransitionRule

val <S, T : Any> IDirectedGraph<S, T>.toTransitionRules: List<TransitionRule<S, out T>>
    get() = nodes
            .flatMap { leftNode ->
                edges(leftNode)
                        .map { (edge, rightNode) ->
                            transition(leftNode, edge::class, rightNode)
                        }
            }