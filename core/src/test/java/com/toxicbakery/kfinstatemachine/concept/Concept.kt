package com.toxicbakery.kfinstatemachine.concept

import com.toxicbakery.kfinstatemachine.concept.FiniteStateMachine.Companion.transition
import com.toxicbakery.kfinstatemachine.concept.StateMachine.TransitionRule
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

enum class State {
    LOGIN_PROMPT,
    AUTHORIZING,
    AUTHORIZED
}

data class SubmitLogin(
        val username: String,
        val password: String
)

data class AuthResponse(
        val code: Int
)

interface StateMachine<out S> {

    val state: S

    fun performTransition(transition: Any)

    data class TransitionRule<out S, T : Any>(
            val oldState: S,
            val transition: KClass<T>,
            val newState: S,
            val test: (value: Any) -> Boolean = { true },
            private val onlyIf: (transition: T) -> Boolean = { true },
            private val doAction: (transition: T) -> Unit = {}
    ) {

        fun onlyIf(func: (transition: T) -> Boolean): TransitionRule<S, T> = copy(onlyIf = func)

        fun doAction(func: (transition: T) -> Unit): TransitionRule<S, T> = copy(doAction = func)

        @Suppress("UNCHECKED_CAST")
        fun transition(transition: Any): Boolean =
                onlyIf(transition as T).apply {
                    if (this) doAction(transition)
                }

    }

}

class FiniteStateMachine<out S>(
        initialState: S,
        private vararg val transitionRules: TransitionRule<S, *>
) : StateMachine<S> {

    private var _state: S = initialState

    private val edges: List<TransitionRule<S, *>>
        get() = transitionRules
                .filter { transitionRule -> transitionRule.oldState == _state }

    override val state: S
        get() = _state

    override fun performTransition(transition: Any) {
        _state = edges.filter { transition.javaClass.isAssignableFrom(it.transition.java) }
                .firstOrNull { it.transition(transition) }
                ?.newState
                ?: throw Exception("Invalid transition `$transition` for state `$_state`")
    }

    companion object {
        fun <S, T : Any> transition(oldState: S, transition: KClass<T>, newState: S): TransitionRule<S, T> =
                TransitionRule(
                        oldState = oldState,
                        transition = transition,
                        newState = newState)
    }

}

class Concept {

    @Test
    fun concept() {
        val finiteStateMachine = FiniteStateMachine(
                State.LOGIN_PROMPT,
                transition(State.LOGIN_PROMPT, SubmitLogin::class, State.AUTHORIZING)
                        .doAction { println("authorizing") },
                transition(State.AUTHORIZING, AuthResponse::class, State.LOGIN_PROMPT)
                        .onlyIf { authResponse: AuthResponse -> authResponse.code == 401 }
                        .doAction { println("auth failed") },
                transition(State.AUTHORIZING, AuthResponse::class, State.LOGIN_PROMPT)
                        .onlyIf { authResponse: AuthResponse -> authResponse.code == 404 }
                        .doAction { println("auth not found") },
                transition(State.AUTHORIZING, AuthResponse::class, State.AUTHORIZED)
                        .onlyIf { authResponse -> authResponse.code == 200 }
                        .doAction { println("auth succeeded") }
        )

        assertEquals(State.LOGIN_PROMPT, finiteStateMachine.state)

        finiteStateMachine.performTransition(SubmitLogin("user", "wrong password"))
        finiteStateMachine.performTransition(AuthResponse(401))
        finiteStateMachine.performTransition(SubmitLogin("user", "correct password"))
        finiteStateMachine.performTransition(AuthResponse(200))
    }

}