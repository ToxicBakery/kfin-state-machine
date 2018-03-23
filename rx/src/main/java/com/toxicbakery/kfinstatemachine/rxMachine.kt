package com.toxicbakery.kfinstatemachine

import io.reactivex.Maybe
import io.reactivex.Observable

val <F : FiniteState, T : Transition> StateMachine<F, T>.stateObservable: Observable<F>
    get() = createObservableTransitionListener(
            init = Maybe.just(state),
            callback = { _, target -> target })

val <F : FiniteState, T : Transition> StateMachine<F, T>.transitionObservable: Observable<Pair<T, F>>
    get() = createObservableTransitionListener(
            callback = { transition, target -> Pair(transition, target) })

inline fun <F : FiniteState, T : Transition, O> StateMachine<F, T>.createObservableTransitionListener(
        init: Maybe<O> = Maybe.empty(),
        crossinline callback: (transition: T, target: F) -> O
): Observable<O> = Observable.create { emitter ->
    object : TransitionListener<F, T> {
        override fun onTransition(transition: T, target: F) {
            emitter.onNext(callback(transition, target))
        }
    }.also { listener ->
        init.subscribe(emitter::onNext)
        addListener(listener)
        emitter.setCancellable {
            removeListener(listener)
        }
    }
}