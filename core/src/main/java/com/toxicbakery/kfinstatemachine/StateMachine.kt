package com.toxicbakery.kfinstatemachine

/**
 * State machine interface that requires the ability to transition finite states.
 */
interface StateMachine {

    /**
     * Transition the machine.
     */
    fun performTransition(transition: Transition)

    companion object {

        /**
         * Invalid initial state of a newly created machine.
         */
        val initialState: FiniteState = object : FiniteState {}
    }

}