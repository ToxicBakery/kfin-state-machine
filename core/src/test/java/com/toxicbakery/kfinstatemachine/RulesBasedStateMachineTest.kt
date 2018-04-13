package com.toxicbakery.kfinstatemachine

import com.toxicbakery.kfinstatemachine.Energy.Kinetic
import com.toxicbakery.kfinstatemachine.Energy.Potential
import com.toxicbakery.kfinstatemachine.EnergyTransition.Release
import com.toxicbakery.kfinstatemachine.EnergyTransition.Store
import com.toxicbakery.kfinstatemachine.RulesBasedStateMachine.Companion.transition
import com.toxicbakery.kfinstatemachine.RulesBasedStateMachineTest.Login.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class RulesBasedStateMachineTest {

    enum class Login : FiniteState {
        PROMPT,
        AUTHORIZING,
        AUTHORIZED;

        override val id: String
            get() = name
    }

    sealed class HttpCode(val code: Int) {
        object Ok : HttpCode(200)
        object NotAuthorized : HttpCode(401)
    }

    data class Credentials(
            val username: String,
            val password: String)

    @Test
    fun performTransition() {
        val stateMachine = RulesBasedStateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(Potential, stateMachine.state)

        stateMachine.performTransition(Release)
        assertEquals(Kinetic, stateMachine.state)

        stateMachine.performTransition(Store)
        assertEquals(Potential, stateMachine.state)
    }

    @Test
    fun performTransition_withActions() {
        var externalStateTracking: Energy = Potential
        val stateMachine = RulesBasedStateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic)
                        .doAction { _, _ -> externalStateTracking = Kinetic },
                transition(Kinetic, Store::class, Potential)
                        .doAction { _, _ -> externalStateTracking = Potential })

        assertEquals(Potential, stateMachine.state)
        assertEquals(Potential, externalStateTracking)

        stateMachine.performTransition(Release)
        assertEquals(Kinetic, stateMachine.state)
        assertEquals(Kinetic, externalStateTracking)

        stateMachine.performTransition(Store)
        assertEquals(Potential, stateMachine.state)
        assertEquals(Potential, externalStateTracking)
    }

    @Test
    fun performTransition_withRules() {
        val semaphore = Semaphore(0)
        val stateMachine = RulesBasedStateMachine(
                PROMPT,
                transition(PROMPT, Credentials::class, AUTHORIZING)
                        .doAction { machine, credentials ->
                            attemptLogin(credentials)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe { httpCode ->
                                        machine.performTransition(httpCode)
                                        semaphore.release()
                                    }
                        },
                transition(AUTHORIZING, HttpCode::class, AUTHORIZED)
                        .onlyIf { it.code == 200 },
                transition(AUTHORIZING, HttpCode::class, PROMPT)
                        .onlyIf { it.code == 401 })

        // Attempt auth with incorrect password
        stateMachine.performTransition(Credentials("user", "incorrect password"))
        assertEquals(AUTHORIZING, stateMachine.state)

        // Wait for login to complete
        semaphore.tryAcquire(1, TimeUnit.SECONDS)
        assertEquals(PROMPT, stateMachine.state)

        // Attempt auth with correct password
        stateMachine.performTransition(Credentials("user", "correct password"))
        assertEquals(AUTHORIZING, stateMachine.state)

        // Wait for login to complete
        semaphore.tryAcquire(1, TimeUnit.SECONDS)
        assertEquals(AUTHORIZED, stateMachine.state)
    }

    @Test
    fun performTransition_withError() {
        val stateMachine = RulesBasedStateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        try {
            stateMachine.performTransition(Store)
            fail("Exception expected")
        } catch (e: Exception) {
            assertTrue(e.message?.startsWith("Invalid transition ") ?: false)
        }
    }

    @Test
    fun availableTransitions() {
        val stateMachine = RulesBasedStateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(
                setOf(Release::class),
                stateMachine.availableTransitions)

        stateMachine.performTransition(Release)

        assertEquals(
                setOf(Store::class),
                stateMachine.availableTransitions)
    }

    @Test
    fun transitionsForTargetState() {
        val stateMachine = RulesBasedStateMachine(
                Potential,
                transition(Potential, Release::class, Kinetic),
                transition(Kinetic, Store::class, Potential))

        assertEquals(
                setOf(Release::class),
                stateMachine.transitionsForTargetState(Kinetic))

        assertEquals(
                setOf<KClass<*>>(),
                stateMachine.transitionsForTargetState(Potential))

        stateMachine.performTransition(Release)

        assertEquals(
                setOf(Store::class),
                stateMachine.transitionsForTargetState(Potential))

        assertEquals(
                setOf<KClass<*>>(),
                stateMachine.transitionsForTargetState(Kinetic))
    }

    private fun attemptLogin(credentials: Credentials): Observable<HttpCode> =
            Observable.just(credentials)
                    .doOnEach({ _ ->
                        Thread.sleep(10)
                    })
                    .map {
                        when (it) {
                            Credentials("user", "correct password") -> HttpCode.Ok
                            else -> HttpCode.NotAuthorized
                        }
                    }

}