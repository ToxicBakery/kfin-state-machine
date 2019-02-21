package com.toxicbakery.kfinstatemachine.graph

/**
 * Map all unique acyclic paths from a given starting point.
 *
 * @param start the node to begin the search for unique paths through the graph
 */
fun <N, E> IDirectedGraph<N, E>.mapAcyclicPaths(
        start: N
): Set<List<N>> = mapAcyclicPaths(mutableListOf(start))
        .minus<List<N>>(listOf(start))

/**
 * Recursively walk unique acyclic paths from a given path and map of known paths.
 *
 * @param currentPath path to be walked
 * @param pathSet paths previously walked
 */
private fun <N, E> IDirectedGraph<N, E>.mapAcyclicPaths(
        currentPath: List<N>,
        pathSet: MutableSet<List<N>> = mutableSetOf()
): Set<List<N>> = edges(currentPath.last()) { mapOf() }
        .let { edges ->
            if (edges.isNotEmpty()) {
                edges.forEach { edge ->
                    when {
                        // Avoid loops and end the path
                        currentPath.contains(edge.value) -> pathSet.add(currentPath)
                        // Copy the path and start a recursive search
                        else -> mapAcyclicPaths(
                                currentPath = currentPath.plus(edge.value),
                                pathSet = pathSet
                        )
                    }
                }
            } else pathSet.add(currentPath)
            return pathSet
        }
