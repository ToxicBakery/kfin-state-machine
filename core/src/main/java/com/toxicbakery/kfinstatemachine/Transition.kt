package com.toxicbakery.kfinstatemachine

/**
 * Transitions describe state changing events. A transition is the only valid way to move between finite states or init
 * a state machine.
 */
interface Transition {

    /**
     * Name of the transition. The event usually describes the action of transitioning between two states.
     */
    val event: String
}