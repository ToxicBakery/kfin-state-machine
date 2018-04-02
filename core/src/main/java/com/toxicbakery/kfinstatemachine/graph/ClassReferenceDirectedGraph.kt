package com.toxicbakery.kfinstatemachine.graph

import com.toxicbakery.kfinstatemachine.FiniteState
import com.toxicbakery.kfinstatemachine.Transition
import kotlin.reflect.KClass

open class ClassReferenceDirectedGraph<N : KClass<*>, L : KClass<*>> internal constructor(
        private val mappedEdges: Map<StateReference<N>, Map<TransitionReference<L>, StateReference<N>>>
) : IDirectedGraph<N, L> {


    private val rootKeys: List<N>
        get() = mappedEdges.keys
                .map(StateReference<N>::reference)

    private val rootKeysMap: Map<N, StateReference<N>> =
            mappedEdges.keys
                    .map { it.reference to it }
                    .toMap()

    override val nodes: Set<N> =
            mappedEdges.values
                    .flatMap(Map<TransitionReference<L>, StateReference<N>>::values)
                    .map(StateReference<N>::reference)
                    .plus(rootKeys)
                    .toSet()

    override fun nodeTransitions(node: N): Set<L> =
            nodeEdges(node).keys

    @Suppress("UNCHECKED_CAST")
    override fun nodeEdges(node: N, default: () -> Map<L, N>): Map<L, N> =
            (mappedEdges[rootKeysMap[node]] ?: default())
                    .map {
                        Pair((it.key as TransitionReference<L>).reference,
                                (it.value as StateReference<N>).reference)
                    }
                    .toMap()

    companion object {
        internal data class StateReference<out T : KClass<*>>(
                val reference: T
        ) : FiniteState {
            override val id: String = reference.java.name
        }

        internal data class TransitionReference<out T : KClass<*>>(
                val reference: T
        ) : Transition {
            override val event: String = reference.java.name
        }

        @Suppress("UNCHECKED_CAST")
        fun <N : KClass<*>, T : KClass<*>> newInstance(
                mappedEdges: Map<*, *>
        ): ClassReferenceDirectedGraph<N, T> =
                (mappedEdges as Map<N, Map<T, N>>)
                        .map {
                            Pair(StateReference(it.key),
                                    it.value
                                            .map {
                                                Pair(TransitionReference(it.key),
                                                        StateReference(it.value))
                                            }
                                            .toMap())
                        }
                        .toMap()
                        .let { ClassReferenceDirectedGraph(it) }
    }
}