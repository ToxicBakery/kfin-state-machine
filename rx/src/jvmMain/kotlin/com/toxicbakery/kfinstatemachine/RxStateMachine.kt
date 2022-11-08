package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.TransitionEvent.EnterTransition
import com.toxicbakery.kfinstatemachine.TransitionEvent.ExitTransition
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * An observable that reports transition events before and after they are applied to the machine.
 */
val <S, T : Any> StateMachine<S, T>.stateObservable: Observable<TransitionEvent<S, T>>
    get() = Observable.create { emitter ->
        val rxCallback = RxStateCallback(emitter)
        registerCallback(rxCallback)
        emitter.setCancellable { unregisterCallback(rxCallback) }
    }

val <S, T : Any> StateMachine<S, T>.enterTransitionObservable: Observable<EnterTransition<S, T>>
    get() = stateObservable
        .filter { event -> event is EnterTransition<S, T> }
        .map { event -> event as EnterTransition<S, T> }

val <S, T : Any> StateMachine<S, T>.exitTransitionObservable: Observable<ExitTransition<S, T>>
    get() = stateObservable
        .filter { event -> event is ExitTransition<S, T> }
        .map { event -> event as ExitTransition<S, T> }

sealed class TransitionEvent<S, T : Any> {

    /**
     * Event signal when a transition is in progress.
     *
     * @param currentState the current state of the machine
     * @param transition the transition that initiated the state change
     * @param targetState the resulting state of this transition
     */
    data class EnterTransition<S, T : Any>(
        val stateMachine: StateMachine<S, T>,
        val currentState: S,
        val transition: T,
        val targetState: S
    ) : TransitionEvent<S, T>()

    /**
     * Event signal when a transition has completed.
     *
     * @param previousState the previous state of the machine before the transition was applied
     * @param transition the transition that initiated the state change
     * @param currentState the resulting state of this transition
     */
    data class ExitTransition<S, T : Any>(
        val stateMachine: StateMachine<S, T>,
        val previousState: S,
        val transition: T,
        val currentState: S
    ) : TransitionEvent<S, T>()

}

/**
 * Captures machine state changes and reports them to the observable.
 *
 * @param emitter of the observable to report to
 */
private class RxStateCallback<S, T : Any>(
    private val emitter: ObservableEmitter<TransitionEvent<S, T>>
) : TransitionCallback<S, T> {

    override fun enteringState(
        stateMachine: StateMachine<S, T>,
        currentState: S,
        transition: T,
        targetState: S
    ) = emitter.onNext(EnterTransition(stateMachine, currentState, transition, targetState))

    override fun enteredState(
        stateMachine: StateMachine<S, T>,
        previousState: S,
        transition: T,
        currentState: S
    ) = emitter.onNext(ExitTransition(stateMachine, previousState, transition, currentState))

}
