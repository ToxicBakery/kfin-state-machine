## Core

Core library providing a base state machine implementation and graph features for building machines.

### Sample Usage

[See Main README](../)

### Install

Includes base state machine implementation

```groovy
compile "com.ToxicBakery.kfinstatemachine:core:2.+"
```

## RxJava

Rx bindings for listening to state changes and transition events of a machine.

### Sample Usage

```kotlin
val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))
                .let { stateMachine -> RxStateMachine(stateMachine) }

// Listen for state changes
stateMachine.observable.subscribe {...}
```

### Install

Includes [Core](core) dependency

```groovy
compile "com.ToxicBakery.kfinstatemachine:rx:2.+"
```