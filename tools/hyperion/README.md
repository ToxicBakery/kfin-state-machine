# Hyperion
Plugin for hyperion to assist in monitoring state machines.

## Sample Usage
```kotlin
// Register
KfinPlugin.registerMachine(stateMachineId, stateMachine)

// Unregister
KfinPlugin.unregisterMachine(stateMachineId)
```

## Install
```groovy
debugImplementation "com.ToxicBakery.kfinstatemachine:hyperion:2.+"
releaseImplementation "com.ToxicBakery.kfinstatemachine:hyperion-no-op:2.+"
```