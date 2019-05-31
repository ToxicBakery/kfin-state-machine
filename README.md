# Kotlin Finite State Machine [![CircleCI](https://circleci.com/gh/ToxicBakery/kfin-state-machine.svg?style=svg)](https://circleci.com/gh/ToxicBakery/kfin-state-machine) [![codecov](https://codecov.io/gh/ToxicBakery/kfin-state-machine/branch/master/graph/badge.svg)](https://codecov.io/gh/ToxicBakery/kfin-state-machine) [![Maven Central](https://img.shields.io/maven-central/v/com.ToxicBakery.kfinstatemachine/core.svg)](https://oss.sonatype.org/content/repositories/releases/com/ToxicBakery/kfinstatemachine/core/)
Kotlin library for creating finite state machines.

> It is an abstract machine that can be in exactly one of a finite number of states at any given time. The FSM can change from one state to another in response to some external inputs; the change from one state to another is called a transition. An FSM is defined by a list of its states, its initial state, and the conditions for each transition.  
> &mdash; [Wikipedia - Finite-state machine][1]

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

val stateMachine = StateMachine(
        Potential,
        Potential onEvent Release::class resultsIn Kinetic,
        Kinetic onEvent Store::class resultsIn Potential
)

// Get the current state, will initially return `Potential`
machine.state

// Move the machine to `Kinetic`
machine.transition(Release)
```

## Samples
Samples intend to show various ways the library might be used.

 * [Dungeon](samples/dungeon) - MUD style application allowing a player to explore a randomly generated dungeon
 * [Stopwatch](samples/stopwatch) - Simple stopwatch that counts from zero at one second intervals until stopped.

## Install
Kfin is now a Kotlin Multiplatform project supporting `js`, `jvm`, and `jvmRx` platforms.

[Core](core) - includes base state machine implementation
```groovy
compile "com.ToxicBakery.kfinstatemachine:kfin-<platform>:4.+"
```

[Graph](graph) - includes directed graph implementation which can be used to create large state machines more easily
```groovy
compile "com.ToxicBakery.kfinstatemachine:graph-<platform>:4.+"
```

[1]:https://en.wikipedia.org/wiki/Finite-state_machine

## Build
The library depends on gradle for compilation and requires JDK 8 or higher.

`./gradlew build`

## Considerations
This library originally took a classical approach using a directed graph for state and callbacks for listening to state and transition changes. Thanks to a talk given by Ray Ryan from Square, I created an implementation generally following their state machine API and reworked my examples to fit. The exact slide can be found [here](https://youtu.be/mvBVkU2mCF4?t=2266) and the talk in its whole is worth the watch.
