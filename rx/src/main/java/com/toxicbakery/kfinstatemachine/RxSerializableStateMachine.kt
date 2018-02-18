package com.toxicbakery.kfinstatemachine

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * Full featured state machine that also implements serialization.
 */
open class RxSerializableStateMachine(
        /**
         * Initial state of the machine
         */
        val initialState: FiniteState
) : StateMachine, Serializable {

    private var _currentState: Subject<FiniteState> = BehaviorSubject.createDefault(initialState)

    /**
     * Observable for current state changes
     */
    val currentStateObservable: Observable<FiniteState>
        get() = _currentState

    /**
     * Transition the machine to a given state.
     */
    override fun transition(transitionState: FiniteState) {
        val currentState = _currentState.blockingFirst()
        if (currentState == transitionState) throw IllegalTransitionToCurrentState(currentState)
        currentState.let(FiniteState::transitions)
                .filter { transition -> transition.isInstance(transitionState) }
                .also { transitionMatches: List<KClass<out FiniteState>> ->
                    if (transitionMatches.isEmpty())
                        throw IllegalTransitionToInvalidState(currentState, transitionState)

                    _currentState.onNext(transitionState)
                }
    }

}