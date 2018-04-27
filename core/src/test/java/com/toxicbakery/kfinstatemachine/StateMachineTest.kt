package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.Energy.Potential
import com.toxicbakery.kfinstatemachine.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.EnergyTransition.Store
import com.toxicbakery.kfinstatemachine.StateMachine.Companion.transition
import com.toxicbakery.kfinstatemachine.StateMachineTest.Login.*
import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.KClass

class StateMachineTest {

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
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(Potential, stateMachine.state)

        stateMachine.transition(Release)
        assertEquals(Kinetic, stateMachine.state)

        stateMachine.transition(Store)
        assertEquals(Potential, stateMachine.state)
    }

    @Test
    fun performTransition_withActions() {
        var externalStateTracking: Energy = Potential
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic)
                        .reaction { _, _ -> externalStateTracking = Kinetic },
                transition(Kinetic, Store::class, Potential)
                        .reaction { _, _ -> externalStateTracking = Potential })

        assertEquals(Potential, stateMachine.state)
        assertEquals(Potential, externalStateTracking)

        stateMachine.transition(Release)
        assertEquals(Kinetic, stateMachine.state)
        assertEquals(Kinetic, externalStateTracking)

        stateMachine.transition(Store)
        assertEquals(Potential, stateMachine.state)
        assertEquals(Potential, externalStateTracking)
    }

    @Test
    fun performTransition_withRules() {
        class LoginMachine {

            private val stateMachine: StateMachine<Login>
            private val _steps: MutableList<Login> = mutableListOf(PROMPT)

            val steps: List<Login>
                get() = _steps

            init {
                stateMachine = StateMachine(
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
                                .reaction { _, _ -> _steps.add(PROMPT) })
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
    fun performTransition_withError() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        try {
            stateMachine.transition(Store)
            fail("Exception expected")
        } catch (e: Exception) {
            assertTrue(e.message?.startsWith("Invalid transition ") ?: false)
        }
    }

    @Test
    fun availableTransitions() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(
                setOf(Release::class),
                stateMachine.transitions)

        stateMachine.transition(Release)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitions)
    }

    @Test
    fun transitionsForTargetState() {
        val stateMachine = StateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(
                setOf(Release::class),
                stateMachine.transitionsTo(Kinetic))

        assertEquals(
                setOf<KClass<*>>(),
                stateMachine.transitionsTo(Potential))

        stateMachine.transition(Release)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitionsTo(Potential))

        assertEquals(
                setOf<KClass<*>>(),
                stateMachine.transitionsTo(Kinetic))
    }

}