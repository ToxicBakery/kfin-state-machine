package com.toxicbakery.kfinstatemachine

/**
 * Identifies a transition acting as the starting point of a state machine.
 */
data class InitialTransition(override val name: String = "InitialTransition") : Transition