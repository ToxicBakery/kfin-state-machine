package com.toxicbakery.kfinstatemachine.graph

import java.util.LinkedList
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * Dijkstra implementation with all weights set to 1.
 *
 * Based on: http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 */
internal class DijkstraAlgorithm<V>(
        graph: DirectedGraph<V>,
        source: GraphNode<V>
) : ShortestPathAlgorithm<V> {

    private val edges: List<GraphEdge<V>> = ArrayList<GraphEdge<V>>(graph.edges)
    private val settledNodes: MutableSet<GraphNode<V>> = hashSetOf()
    private val unSettledNodes: MutableSet<GraphNode<V>> = hashSetOf(source)
    private val predecessors: MutableMap<GraphNode<V>, GraphNode<V>> = hashMapOf()
    private val distance: MutableMap<GraphNode<V>, Int> = hashMapOf(source to 0)

    init {
        while (unSettledNodes.size > 0) {
            val node = getMinimum(unSettledNodes)
            settledNodes.add(node)
            unSettledNodes.remove(node)
            calculateDistances(node)
        }
    }

    override fun shortestPathTo(target: GraphNode<V>): List<GraphNode<V>> {
        // check if a path exists
        if (!predecessors.containsKey(target)) throw Exception("No path between nodes.")
        val path = LinkedList<GraphNode<V>>().apply { add(target) }
        while (true) {
            predecessors[path.first]
                    ?.apply { path.push(this) }
                    ?: break
        }
        return path
    }

    private fun calculateDistances(node: GraphNode<V>) {
        val adjacentNodes = getNeighbors(node)
        for (target in adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance[target] = getShortestDistance(node) + getDistance(node, target)
                predecessors[target] = node
                unSettledNodes.add(target)
            }
        }
    }

    private fun getDistance(node: GraphNode<V>, target: GraphNode<V>): Int {
        edges.forEach { edge -> if (edge.left == node && edge.right == target) return 1 }
        throw Exception("No path between nodes")
    }

    private fun getNeighbors(node: GraphNode<V>): List<GraphNode<V>> {
        val neighbors = ArrayList<GraphNode<V>>()
        for (edge in edges) {
            if (edge.left == node && !isSettled(edge.right)) neighbors.add(edge.right)
        }
        return neighbors
    }

    private fun getMinimum(vertexes: Set<GraphNode<V>>): GraphNode<V> {
        var minimum: GraphNode<V> = vertexes.first()
        for (vertex in vertexes) {
            if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                minimum = vertex
            }
        }
        return minimum
    }

    private fun isSettled(vertex: GraphNode<V>): Boolean =
            settledNodes.contains(vertex)

    private fun getShortestDistance(destination: GraphNode<V>): Int =
            distance[destination] ?: Integer.MAX_VALUE

}