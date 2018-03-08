package com.toxicbakery.kfinstatemachine.graph

interface ShortestPathAlgorithm<V> {

    /**
     * Based on predefined inputs, find the shortest path to a given node.
     */
    fun shortestPathTo(target: GraphNode<V>): List<GraphNode<V>>

}