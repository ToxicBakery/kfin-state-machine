package com.toxicbakery.kfinstatemachine

sealed class EnergyTransition(override val event: String) : Transition {
    object Store : EnergyTransition("Store")
    object Release : EnergyTransition("Release")
    object InvalidTransition : EnergyTransition("InvalidTransition")
}