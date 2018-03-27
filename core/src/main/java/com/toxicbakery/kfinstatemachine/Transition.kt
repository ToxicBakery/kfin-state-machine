package com.toxicbakery.kfinstatemachine

/**
 * Transitions describe state changing events.
 * A transition is the only valid way to move between finite states.
 */
interface Transition {

    /**
     * Name of the transition.
     */
    val event: String
}