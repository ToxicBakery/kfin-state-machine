# Core
Core library providing a base state machine implementation and graph features for building machines.

## Sample Usage
```kotlin
sealed class Energy(override val id: String) : FiniteState {
    object Kinetic : Energy("kinetic")
    object Potential : Energy("potential")
}

sealed class EnergyTransition(override val event: String) : Transition {
    object Store : EnergyTransition("Store")
    object Release : EnergyTransition("Release")
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
Includes directed graphs and base state machine implementation
```groovy
compile "com.ToxicBakery.kfinstatemachine:core:2.+"
```
