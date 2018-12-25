package com.toxicbakery.kfinstatemachine

sealed class EnergyTransition {
    object Store : EnergyTransition()
    object Release : EnergyTransition()
    object InvalidTransition : EnergyTransition()
}