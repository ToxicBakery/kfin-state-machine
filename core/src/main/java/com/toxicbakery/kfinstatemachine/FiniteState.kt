package com.toxicbakery.kfinstatemachine

/**
 * A state of a machine
 */
interface FiniteState {

    /**
     * Identifier of a state. Identifiers must be unique in a machine.
     */
    val id: String
}