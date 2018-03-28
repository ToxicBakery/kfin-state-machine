package com.toxicbakery.sample.countdown

import com.toxicbakery.sample.countdown.CountdownMachine.TimerEvent.*
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.Semaphore

class CountdownMachineTest {

    @Test
    fun cycle() {
        val semaphore = Semaphore(0)
        val countdownMachine = CountdownMachine()
        val targetInstant = Calendar.getInstance()
                .apply { add(Calendar.SECOND, 1) }
                .time
                .toInstant()

        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                countdownMachine.performTransition(Tick)
                if (Instant.now().isAfter(targetInstant)) {
                    countdownMachine.performTransition(Stop)
                    semaphore.release()
                }
            }
        }

        fun timeRemaining(): Long =
                Math.ceil(targetInstant
                        .minus(Date().time, ChronoUnit.MILLIS)
                        .toEpochMilli() / 1000.0).toLong()
                        .let { time: Long -> if (time < 0) 0 else time }

        countdownMachine.addListener({ transitionEvent ->
            when (transitionEvent.transition) {
                Stop -> timer.cancel()
                is Start -> timer.scheduleAtFixedRate(timerTask, 1, 1000)
                is Tick -> println("Time remaining: ${timeRemaining()}")
            }
        })

        // Start the timer
        countdownMachine.performTransition(Start)

        semaphore.acquire()
    }
}