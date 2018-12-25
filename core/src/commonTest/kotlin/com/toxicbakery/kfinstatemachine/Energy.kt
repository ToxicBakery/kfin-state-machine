package com.toxicbakery.kfinstatemachine

sealed class Energy {
    object Kinetic : Energy()
    object Potential : Energy()
    object InvalidState : Energy()
}