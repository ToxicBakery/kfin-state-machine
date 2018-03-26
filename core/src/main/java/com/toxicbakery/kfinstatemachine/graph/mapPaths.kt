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
    val edges: List<GraphEdge<N, E>> = mappedEdges[currentPath.last()] ?: listOf()
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
