package com.toxicbakery.kfinstatemachine

import java.io.Serializable
import kotlin.reflect.KClass

/**
 * A potential state of a machine that indicates valid transition states.
 */
interface FiniteState : Serializable {

    /**
     * Transitions available to this state.
     */
    val transitions: Set<KClass<out FiniteState>>
}