package com.toxicbakery.kfinstatemachine

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface IStateMachine<out F> {

    /**
     * Current state of the machine.
     */
    val state: F

}