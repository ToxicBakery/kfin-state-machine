package com.toxicbakery.kfinstatemachine.graph

/**
 * Map all unique acyclic paths from a given starting point.
 *
 * @param start the node to begin the search for unique paths through the graph
 */
fun <N, E> IDirectedGraph<N, E>.mapAcyclicPaths(
        start: N
): Set<List<N>> = mapAcyclicPaths(mutableListOf(start))

private fun <N, E> IDirectedGraph<N, E>.mapAcyclicPaths(
        currentPath: MutableList<N>,
        pathSet: MutableSet<List<N>> = mutableSetOf()
): Set<List<N>> = edges(currentPath.last()) { mapOf() }
        .also { edges ->
            if (edges.isNotEmpty()) {
                edges.forEach { edge ->
                    if (currentPath.contains(edge.value)) {
                        // Avoid loops and end the path
                        pathSet.add(currentPath)
                    } else {
                        // Copy the path and start a recursive search
                        currentPath.plus(edge.value)
                                .toMutableList()
                                .let { currentPath ->
                                    mapAcyclicPaths(currentPath = currentPath, pathSet = pathSet)
                                }
                    }
                }
            } else pathSet.add(currentPath)
        }
        .let { pathSet }
