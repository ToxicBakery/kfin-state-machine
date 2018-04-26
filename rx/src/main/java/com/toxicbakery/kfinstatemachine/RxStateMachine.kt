package com.toxicbakery.kfinstatemachine

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlin.reflect.KClass

class RxStateMachine<F : FiniteState>(
        private val stateMachine: IStateMachine<F>
) : IStateMachine<F> {

    private val subject: Subject<F> by lazy { BehaviorSubject.createDefault(state) }

    val observable: Observable<F> = subject

    override val state: F
        get() = stateMachine.state

    override val transitions: Set<KClass<*>>
        get() = stateMachine.transitions

    override fun transition(transition: Any) =
            stateMachine.transition(transition)
                    .also { subject.onNext(state) }

    override fun transitionsTo(targetState: F): Set<KClass<*>> =
            stateMachine.transitionsTo(targetState)

}