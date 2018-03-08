package com.toxicbakery.kfinstatemachine.graph

data class DirectedGraph<N, out E>(
        val edges: Set<GraphEdge<N, E>>,
        private val shortestPathAlgorithmFactory: (DirectedGraph<N, E>, GraphNode<N>) -> ShortestPathAlgorithm<N> =
                { graph, source -> DijkstraAlgorithm(graph, source) }
) : IDirectedGraph<N, E> {

    override val nodes: Set<GraphNode<N>> = edges.flatMap { setOf(it.left, it.right) }
            .toSet()

    override fun shortestPath(
            source: GraphNode<N>,
            destination: GraphNode<N>
    ): List<GraphNode<N>> = shortestPathAlgorithmFactory(this, source).shortestPathTo(destination)

    override fun rightEdgesForValue(nodeValue: N): Set<GraphEdge<N, E>> =
            edges.filter { it.left.value == nodeValue }.toSet()

}
