package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.StateMachine.Companion.transition
import com.toxicbakery.kfinstatemachine.SynchronizedStateMachineTest.Login.*
import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.KClass

class SynchronizedStateMachineTest {

    enum class Login {
        PROMPT,
        AUTHORIZING,
        AUTHORIZED
    }

    sealed class HttpCode {
        object Ok : HttpCode()
        object NotAuthorized : HttpCode()
    }

    data class Credentials(
            val username: String,
            val password: String)

    @Test
    fun performTransition() {
        val stateMachine = SynchronizedStateMachine(StateMachine(
                Energy.Potential,
                transition(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic),
                transition(Energy.Kinetic, EnergyTransition.Store::class, Energy.Potential)))

        assertEquals(Energy.Potential, stateMachine.state)

        stateMachine.transition(EnergyTransition.Release)
        assertEquals(Energy.Kinetic, stateMachine.state)

        stateMachine.transition(EnergyTransition.Store)
        assertEquals(Energy.Potential, stateMachine.state)
    }

    @Test
    fun performTransition_altConstructor() {
        val stateMachine = SynchronizedStateMachine(StateMachine(
                Energy.Potential,
                listOf(
                        transition(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic),
                        transition(Energy.Kinetic, EnergyTransition.Store::class, Energy.Potential)
                )))

        assertEquals(Energy.Potential, stateMachine.state)

        stateMachine.transition(EnergyTransition.Release)
        assertEquals(Energy.Kinetic, stateMachine.state)

        stateMachine.transition(EnergyTransition.Store)
        assertEquals(Energy.Potential, stateMachine.state)
    }

    @Test
    fun performTransition_withActions() {
        var externalStateTracking: Energy = Energy.Potential
        val stateMachine = SynchronizedStateMachine(StateMachine(
                Energy.Potential,
                transition(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic)
                        .reaction { _, _ -> externalStateTracking = Energy.Kinetic },
                transition(Energy.Kinetic, EnergyTransition.Store::class, Energy.Potential)
                        .reaction { _, _ -> externalStateTracking = Energy.Potential }))

        assertEquals(Energy.Potential, stateMachine.state)
        assertEquals(Energy.Potential, externalStateTracking)

        stateMachine.transition(EnergyTransition.Release)
        assertEquals(Energy.Kinetic, stateMachine.state)
        assertEquals(Energy.Kinetic, externalStateTracking)

        stateMachine.transition(EnergyTransition.Store)
        assertEquals(Energy.Potential, stateMachine.state)
        assertEquals(Energy.Potential, externalStateTracking)
    }

    @Test
    fun performTransition_withRules() {
        class LoginMachine {

            private val stateMachine: IStateMachine<Login>
            private val _steps: MutableList<Login> = mutableListOf(PROMPT)

            val steps: List<Login>
                get() = _steps

            init {
                stateMachine = SynchronizedStateMachine(StateMachine(
                        PROMPT,
                        transition(PROMPT, Credentials::class, AUTHORIZING)
                                .reaction { _, credentials ->
                                    _steps.add(AUTHORIZING)
                                    doLogin(credentials)
                                },
                        transition(AUTHORIZING, HttpCode::class, AUTHORIZED)
                                .onlyIf { it === HttpCode.Ok }
                                .reaction { _, _ -> _steps.add(AUTHORIZED) },
                        transition(AUTHORIZING, HttpCode::class, PROMPT)
                                .onlyIf { it === HttpCode.NotAuthorized }
                                .reaction { _, _ -> _steps.add(Login.PROMPT) }))
            }

            fun login(credentials: Credentials) = stateMachine.transition(credentials)

            private fun doLogin(credentials: Credentials) =
                    when (credentials) {
                        Credentials("user", "correct password") -> HttpCode.Ok
                        else -> HttpCode.NotAuthorized
                    }.let(stateMachine::transition)

        }

        val loginMachine = LoginMachine()
        loginMachine.login(Credentials("user", "incorrect password"))
        loginMachine.login(Credentials("user", "correct password"))

        assertEquals(
                listOf(
                        PROMPT,
                        AUTHORIZING,
                        PROMPT,
                        AUTHORIZING,
                        AUTHORIZED),
                loginMachine.steps)
    }

    @Test
    fun performTransition_invalidTransition() {
        val stateMachine = SynchronizedStateMachine(StateMachine(
                Energy.Potential,
                transition(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic),
                transition(Energy.Kinetic, EnergyTransition.Store::class, Energy.Potential)))

        try {
            stateMachine.transition(EnergyTransition.Store)
            fail("Exception expected")
        } catch (e: Exception) {
            assertTrue(e.message?.startsWith("Invalid transition ") ?: false)
        }
    }

    @Test
    fun performTransition_ambiguousTransition() {
        val stateMachine = SynchronizedStateMachine(StateMachine(
                Energy.Potential,
                transition(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic),
                transition<Energy, EnergyTransition.Release>(Energy.Potential, EnergyTransition.Release::class, Energy.Potential),
                transition(Energy.Kinetic, EnergyTransition.Store::class, Energy.Potential)))

        try {
            stateMachine.transition(EnergyTransition.Release)
            fail("Exception expected")
        } catch (e: Exception) {
            assertTrue(e.message?.startsWith("Ambiguous transition ") ?: false)
        }
    }

    @Test
    fun availableTransitions() {
        val stateMachine = SynchronizedStateMachine(StateMachine(
                Energy.Potential,
                transition(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic),
                transition(Energy.Kinetic, EnergyTransition.Store::class, Energy.Potential)))

        assertEquals(
                setOf(EnergyTransition.Release::class),
                stateMachine.transitions)

        stateMachine.transition(EnergyTransition.Release)

        assertEquals(
                setOf(EnergyTransition.Store::class),
                stateMachine.transitions)
    }

    @Test
    fun transitionsForTargetState() {
        val stateMachine = SynchronizedStateMachine(StateMachine(
                Energy.Potential,
                transition(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic),
                transition(Energy.Kinetic, EnergyTransition.Store::class, Energy.Potential)))

        assertEquals(
                setOf(EnergyTransition.Release::class),
                stateMachine.transitionsTo(Energy.Kinetic))

        assertEquals(
                setOf<KClass<*>>(),
                stateMachine.transitionsTo(Energy.Potential))

        stateMachine.transition(EnergyTransition.Release)

        assertEquals(
                setOf(EnergyTransition.Store::class),
                stateMachine.transitionsTo(Energy.Potential))

        assertEquals(
                setOf<KClass<*>>(),
                stateMachine.transitionsTo(Energy.Kinetic))
    }

    @Test
    fun validateRulesRules() {
        TransitionRule(Energy.Potential, EnergyTransition.Release::class, Energy.Kinetic)
                .onlyIf { transition -> transition === EnergyTransition.Release }
                .onlyIf { transition -> transition === EnergyTransition.Release }
                .validate(EnergyTransition.Release)
    }

}