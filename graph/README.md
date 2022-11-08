# Graph

Directed graph implementation which may be useful for creating large machines more quickly.
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
```

## Install

Includes directed graphs

```groovy
compile "com.ToxicBakery.kfinstatemachine:graph:2.+"
```
