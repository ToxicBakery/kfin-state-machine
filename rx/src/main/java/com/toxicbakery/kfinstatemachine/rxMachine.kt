package com.toxicbakery.kfinstatemachine

import io.reactivex.Observable

/**
 * Streams the current state of the machine and future state changes.
 */
val <F : FiniteState, T : Transition> IStateMachine<F, T>.stateChangeObservable: Observable<F>
    get() = Observable.create { emitter ->
        emitter.onNext(state)
        addOnStateChangeListener(emitter::onNext)
                .also { listener -> emitter.setCancellable { removeOnStateChangedListener(listener) } }
    }

/**
 * Streams machine transition events.
 */
val <F : FiniteState, T : Transition> IStateMachine<F, T>.transitionEventObservable: Observable<TransitionEvent<F, T>>
    get() = Observable.create { emitter ->
        addOnTransitionListener(emitter::onNext)
                .also { listener -> emitter.setCancellable { removeOnTransitionListener(listener) } }
    }
