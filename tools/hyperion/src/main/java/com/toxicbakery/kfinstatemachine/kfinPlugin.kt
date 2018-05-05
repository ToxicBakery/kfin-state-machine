package com.toxicbakery.kfinstatemachine

val registeredMachines: List<Pair<String, IStateMachine<*>>>
    get() = KfinPlugin.registeredMachines
            .map { entry -> entry.key to entry.value }
