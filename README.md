# Kotlin Finite State Machine
Kotlin library for creating finite state machines.

> It is an abstract machine that can be in exactly one of a finite number of states at any given time. The FSM can change from one state to another in response to some external inputs; the change from one state to another is called a transition. An FSM is defined by a list of its states, its initial state, and the conditions for each transition.  
> &mdash; [Wikipedia - Finite-state machine][1]

 ## Sample Usage
```kotlin
sealed class Energy(
        override val transitions: Set<KClass<out FiniteState>>
) : FiniteState {
    object Potential : Energy(setOf(Kinetic::class))
    object Kinetic : Energy(setOf(Potential::class))
}

class Usage {

    init {
        val serializableStateMachine = SerializableStateMachine(Potential)
        serializableStateMachine.transition(Kinetic)
    }

}
```

[1]:https://en.wikipedia.org/wiki/Finite-state_machine