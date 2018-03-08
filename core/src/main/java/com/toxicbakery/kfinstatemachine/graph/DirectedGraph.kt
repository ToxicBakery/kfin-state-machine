package com.toxicbakery.kfinstatemachine.graph

data class GraphEdge<out V>(
        val left: GraphNode<V>,
        val right: GraphNode<V>,
        val label: String
)

data class GraphNode<out V>(
        val value: V
)

data class DirectedGraph<V>(
        val edges: Set<GraphEdge<V>>,
        private val shortestPathAlgorithmFactory: (DirectedGraph<V>, GraphNode<V>) -> ShortestPathAlgorithm<V> =
                { graph, source -> DijkstraAlgorithm(graph, source) }
) {

    val nodes: Set<GraphNode<V>> = edges.flatMap { setOf(it.left, it.right) }
            .toSet()

    fun shortestPath(
            source: GraphNode<V>,
            destination: GraphNode<V>
    ): List<GraphNode<V>> = shortestPathAlgorithmFactory(this, source).shortestPathTo(destination)

}

