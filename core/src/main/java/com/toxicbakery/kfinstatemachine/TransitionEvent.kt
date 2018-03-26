package com.toxicbakery.kfinstatemachine

data class TransitionEvent<out F : FiniteState, out T : Transition>(
        val transition: T,
        val targetState: F
)