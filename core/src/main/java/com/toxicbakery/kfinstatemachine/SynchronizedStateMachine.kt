package com.toxicbakery.kfinstatemachine

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.reflect.KClass

class SynchronizedStateMachine<F>(
        private val stateMachine: IStateMachine<F>,
        private val lock: Lock = ReentrantLock(true)
) : IStateMachine<F> {

    override val state: F
        get() = lock.withLock { stateMachine.state }

    override val transitions: Set<KClass<*>>
        get() = lock.withLock { stateMachine.transitions }

    override fun transition(transition: Any) =
            lock.withLock { stateMachine.transition(transition) }

    override fun transitionsTo(targetState: F): Set<KClass<*>> =
            lock.withLock { stateMachine.transitionsTo(targetState) }

}