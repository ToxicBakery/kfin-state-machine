# Kotlin Finite State Machine [![Build Status](https://travis-ci.org/ToxicBakery/kfin-state-machine.svg?branch=master)](https://travis-ci.org/ToxicBakery/kfin-state-machine)
Kotlin library for creating finite state machines.

> It is an abstract machine that can be in exactly one of a finite number of states at any given time. The FSM can change from one state to another in response to some external inputs; the change from one state to another is called a transition. An FSM is defined by a list of its states, its initial state, and the conditions for each transition.  
> &mdash; [Wikipedia - Finite-state machine][1]

## Sample Usage
```kotlin
sealed class Energy(override val id: String) : FiniteState {
    object Kinetic : Energy("kinetic")
    object Potential : Energy("potential")
}

sealed class EnergyTransition(override val event: String) : Transition {
    object Store : EnergyTransition("Store")
    object Release : EnergyTransition("Release")
    object Invalid : EnergyTransition("Invalid")
}

val machine = BaseMachine(
    directedGraph = DirectedGraph(
            edges = setOf(
                    GraphEdge(
                            left = Potential,
                            right = Kinetic,
                            label = Release
                    ),
                    GraphEdge(
                            left = Kinetic,
                            right = Potential,
                            label = Store
                    )
            )
    ),
    initialState = Potential
)

// Get the current state, will initially return `Potential`
machine.state

// Move the machine to `Kinetic`
machine.performTransition(Release)

// Listen for transitions
machine.addListener(object : TransitionListener<Energy, EnergyTransition> {
    override fun onTransition(transition: EnergyTransition, target: Energy) {
        // Perform work before the machine enters the new state
    }
})
```

## Install

Core, includes directed graphs and base state machine implementation
```groovy
compile "com.ToxicBakery.kfinstatemachine:core:2.+"
```

SCXML, includes core dependency
```groovy
compile "com.ToxicBakery.kfinstatemachine:scxml:2.+"
```

Rx support, includes core dependency
```groovy
compile "com.ToxicBakery.kfinstatemachine:rx:2.+"
```

[1]:https://en.wikipedia.org/wiki/Finite-state_machine