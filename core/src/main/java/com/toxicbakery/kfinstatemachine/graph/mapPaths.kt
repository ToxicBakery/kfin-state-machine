package com.toxicbakery.kfinstatemachine.graph

/**
 * Map acyclic paths from a given starting point.
 */
fun <N, E> IDirectedGraph<N, E>.mapAcyclicPaths(
        start: N
): Set<List<N>> = mapAcyclicPaths(mutableListOf(start))

/**
 * Recursive search function for finding acyclic paths in a directed graph.
 */
private fun <N, E> IDirectedGraph<N, E>.mapAcyclicPaths(
        currentPath: MutableList<N>,
        pathSet: MutableSet<List<N>> = mutableSetOf()
): Set<List<N>> {
    val edges = exitingEdgesForNode(currentPath.last())
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
fun <N, E> IDirectedGraph<N, E>.exitingEdgesForNode(node: N): Set<GraphEdge<N, E>> =
        edges.filter { it.left == node }.toSet()

/**
 * Search a graphs edges for all edges leaving the node.
 */
fun <N, E> IDirectedGraph<N, E>.exitingEdgesForNodeValue(nodeValue: N): Set<GraphEdge<N, E>> =
        edges.filter { it.left == nodeValue }.toSet()
