# Graph Util
Directed graph utility for converting directed graphs to state machines.
The [Dungeon](samples/dungeon) sample demonstrates a use case of this module.

## Sample Usage
```kotlin
sealed class Energy {
    object Kinetic : Energy()
    object Potential : Energy()
}

sealed class EnergyTransition {
    object Store : EnergyTransition()
    object Release : EnergyTransition()
}

DirectedGraph(
        mapOf(
                Potential to mapOf(Release to Kinetic),
                Kinetic to mapOf(Store to Potential)
        ))
        .transitionRules
```

## Install
Includes directed graphs
```groovy
compile "com.ToxicBakery.kfinstatemachine:graph-util:2.+"
```
