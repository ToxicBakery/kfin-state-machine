package com.toxicbakery.kfinstatemachine

sealed class Energy(override val id: String) : FiniteState {
    object Kinetic : Energy("Kinetic")
    object Potential : Energy("Potential")
    object InvalidState : Energy("InvalidState")
}