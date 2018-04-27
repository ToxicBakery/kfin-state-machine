package com.toxicbakery.sample.counter

import com.toxicbakery.sample.counter.TimerEvent.Start
import com.toxicbakery.sample.counter.TimerEvent.Stop
import com.toxicbakery.sample.counter.TimerState.Stopped
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class StopwatchMachineTest {

    @Test
    fun emulateApplication() {
        val countdownMachine = StopwatchMachine()
        countdownMachine.transition(Start)
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(5)
                .toList()
                .blockingGet()

        countdownMachine.transition(Stop)
        assertEquals(Stopped, countdownMachine.state)
    }
}