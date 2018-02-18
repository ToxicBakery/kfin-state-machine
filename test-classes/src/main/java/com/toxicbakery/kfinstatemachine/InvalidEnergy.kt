package com.toxicbakery.kfinstatemachine

import kotlin.reflect.KClass

object InvalidEnergy : FiniteState {
    override val transitions: Set<KClass<out FiniteState>> = setOf()
}