# RxJava
Rx bindings for listening to state changes and transition events of a machine.

## Sample Usage
```kotlin
machine.stateObservable
   .subscribe { state -> }

machine.transitionObservable
   .subscribe { transitionEvent ->  }
```

## Install
Includes [Core](core) dependency
```groovy
compile "com.ToxicBakery.kfinstatemachine:rx:2.+"
```