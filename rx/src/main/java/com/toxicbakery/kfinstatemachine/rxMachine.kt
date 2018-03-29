package com.toxicbakery.kfinstatemachine

import io.reactivex.Maybe
import io.reactivex.Observable

/**
 * Streams the current state of the machine and future state changes.
 */
val <F : FiniteState, T : Transition> StateMachine<F, T>.stateObservable: Observable<F>
    get() = createObservableTransitionListener(
            init = Maybe.just(state),
            callback = { transitionEvent -> transitionEvent.targetState })

/**
 * Streams machine transition changes.
 */
val <F : FiniteState, T : Transition> StateMachine<F, T>.transitionObservable: Observable<TransitionEvent<F, T>>
    get() = createObservableTransitionListener(
            callback = { transitionEvent -> transitionEvent })

/**
 * Utility method for creating an observable from a machine callback.
 * The callback is unregistered when the stream is disposed.
 *
 * @param init optional initial value to pass through the stream
 * @param callback handler for sending callbacks through the stream
 */
fun <F : FiniteState, T : Transition, O> StateMachine<F, T>.createObservableTransitionListener(
        init: Maybe<O> = Maybe.empty(),
        callback: (transitionEvent: TransitionEvent<F, T>) -> O
): Observable<O> = Observable.create { emitter ->
    init.subscribe(emitter::onNext)
    addOnTransitionListener({ transitionEvent -> emitter.onNext(callback(transitionEvent)) })
            .also { listener -> emitter.setCancellable { removeOnTransitionListener(listener) } }
}