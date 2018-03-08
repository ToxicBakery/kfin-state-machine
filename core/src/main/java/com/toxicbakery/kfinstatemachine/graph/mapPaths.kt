package com.toxicbakery.kfinstatemachine.graph

/**
 * Map acyclic paths from a given starting point.
 */
fun <N, E> DirectedGraph<N, E>.mapAcyclicPaths(
        start: GraphNode<N>
): Set<List<GraphNode<N>>> = mapAcyclicPaths(mutableListOf(start))

/**
 * Recursive search function for finding acyclic paths in a directed graph.
 */
private fun <N, E> DirectedGraph<N, E>.mapAcyclicPaths(
        currentPath: MutableList<GraphNode<N>>,
        pathSet: MutableSet<List<GraphNode<N>>> = mutableSetOf()
): Set<List<GraphNode<N>>> {
    val edges = rightEdgesForNode(currentPath.last())
    if (edges.isNotEmpty()) {
        edges.forEach { edge ->
            if (currentPath.contains(edge.right)) {
                // Avoid loops and end the path
                pathSet.add(currentPath)
            } else {
                // Copy the path and start a recursive search
                currentPath.plus(edge.right)
                        .toMutableList()
                        .let { mapAcyclicPaths(it, pathSet) }
            }
        }
    } else pathSet.add(currentPath)

    return pathSet
}

/**
 * Helper function for calling value based edge discovery.
 */
fun <N, E> IDirectedGraph<N, E>.rightEdgesForNode(node: GraphNode<N>): Set<GraphEdge<N, E>> =
        rightEdgesForValue(node.value)
