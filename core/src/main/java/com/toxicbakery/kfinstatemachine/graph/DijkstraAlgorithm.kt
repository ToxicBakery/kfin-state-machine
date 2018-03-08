package com.toxicbakery.kfinstatemachine.graph

import java.util.LinkedList
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * Dijkstra implementation with all weights set to 1.
 *
 * Based on: http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 */
internal class DijkstraAlgorithm<N, E>(
        graph: DirectedGraph<N, E>,
        source: GraphNode<N>
) : ShortestPathAlgorithm<N> {

    private val edges: List<GraphEdge<N, E>> = ArrayList<GraphEdge<N, E>>(graph.edges)
    private val settledNodes: MutableSet<GraphNode<N>> = hashSetOf()
    private val unSettledNodes: MutableSet<GraphNode<N>> = hashSetOf(source)
    private val predecessors: MutableMap<GraphNode<N>, GraphNode<N>> = hashMapOf()
    private val distance: MutableMap<GraphNode<N>, Int> = hashMapOf(source to 0)

    init {
        while (unSettledNodes.size > 0) {
            val node = getMinimum(unSettledNodes)
            settledNodes.add(node)
            unSettledNodes.remove(node)
            calculateDistances(node)
        }
    }

    override fun shortestPathTo(target: GraphNode<N>): List<GraphNode<N>> {
        // check if a path exists
        if (!predecessors.containsKey(target)) throw Exception("No path between nodes.")
        val path = LinkedList<GraphNode<N>>().apply { add(target) }
        while (true) {
            predecessors[path.first]
                    ?.apply { path.push(this) }
                    ?: break
        }
        return path
    }

    private fun calculateDistances(node: GraphNode<N>) {
        val adjacentNodes = getNeighbors(node)
        for (target in adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance[target] = getShortestDistance(node) + getDistance(node, target)
                predecessors[target] = node
                unSettledNodes.add(target)
            }
        }
    }

    private fun getDistance(node: GraphNode<N>, target: GraphNode<N>): Int {
        edges.forEach { edge -> if (edge.left == node && edge.right == target) return 1 }
        throw Exception("No path between nodes")
    }

    private fun getNeighbors(node: GraphNode<N>): List<GraphNode<N>> {
        val neighbors = ArrayList<GraphNode<N>>()
        for (edge in edges) {
            if (edge.left == node && !isSettled(edge.right)) neighbors.add(edge.right)
        }
        return neighbors
    }

    private fun getMinimum(vertexes: Set<GraphNode<N>>): GraphNode<N> {
        var minimum: GraphNode<N> = vertexes.first()
        for (vertex in vertexes) {
            if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                minimum = vertex
            }
        }
        return minimum
    }

    private fun isSettled(vertex: GraphNode<N>): Boolean =
            settledNodes.contains(vertex)

    private fun getShortestDistance(destination: GraphNode<N>): Int =
            distance[destination] ?: Integer.MAX_VALUE

}