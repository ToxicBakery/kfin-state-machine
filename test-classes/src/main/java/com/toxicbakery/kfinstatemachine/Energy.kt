package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

sealed class Energy(
        override val transitions: Set<KClass<out FiniteState>>
) : FiniteState {
    object Potential : Energy(setOf(Kinetic::class))
    object Kinetic : Energy(setOf(Potential::class))
}