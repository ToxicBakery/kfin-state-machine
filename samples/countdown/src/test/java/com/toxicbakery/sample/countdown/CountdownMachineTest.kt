package com.toxicbakery.sample.countdown

import com.toxicbakery.sample.countdown.CountdownMachine.TimerEvent
import com.toxicbakery.sample.countdown.CountdownMachine.TimerState
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.Semaphore

class CountdownMachineTest {

    private val oneSecondFromNow: Instant
        get() = Instant.now().plusSeconds(1)

    private fun timeRemaining(targetInstant: Instant) =
            Math.ceil(targetInstant
                    .minus(Date().time, ChronoUnit.MILLIS)
                    .toEpochMilli() / 1000.0).toLong()
                    .let { time: Long -> if (time < 0) 0 else time }

    @Test
    fun cycle() {
        val semaphore = Semaphore(0)
        val countdownMachine = CountdownMachine()
                .apply {
                    addOnTransitionListener { transitionEvent ->
                        if (transitionEvent.targetState == TimerState.Stopped)
                            semaphore.release()
                    }
                }

        // Use the machine
        oneSecondFromNow.also { targetInstant ->
            val stateListener = countdownMachine.addOnStateChangeListener { timerState ->
                when (timerState) {
                    CountdownMachine.TimerState.Stopped -> Unit
                    CountdownMachine.TimerState.Running ->
                        if (timeRemaining(targetInstant) <= 0) countdownMachine.performTransition(TimerEvent.Stop)
                }
            }

            val transitionListener = countdownMachine.addOnTransitionListener { transitionEvent ->
                when (transitionEvent.transition) {
                    TimerEvent.Tick ->
                        timeRemaining(targetInstant)
                                .also { timeRemaining -> println("Time remaining $timeRemaining") }
                }
            }

            countdownMachine.performTransition(TimerEvent.Start)
            semaphore.acquire()
            countdownMachine.removeOnStateChangedListener(stateListener)
            countdownMachine.removeOnTransitionListener(transitionListener)
        }
    }
}