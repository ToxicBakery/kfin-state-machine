package com.toxicbakery.sample.counter

import com.toxicbakery.kfinstatemachine.StateMachine
import com.toxicbakery.sample.counter.TimerEvent.*
import com.toxicbakery.sample.counter.TimerState.Running
import com.toxicbakery.sample.counter.TimerState.Stopped
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit

enum class TimerState {
    Stopped,
    Running
}

sealed class TimerEvent {
    object Stop : TimerEvent()
    object Start : TimerEvent()
    data class Tick(val tick: Long) : TimerEvent()
}

class StopwatchMachine : StateMachine<TimerState>(
        Stopped,
        transition(Stopped, Start::class, Running)
                .reaction { machine, _ -> (machine as StopwatchMachine).startTicker() },
        transition(Running, Tick::class, Running)
                .reaction { _, tick -> println(tick.tick) },
        transition(Running, Stop::class, Stopped)
                .reaction { machine, _ -> (machine as StopwatchMachine).stopTicker() }
) {

    private var tickerDisposable: Disposable = Disposables.disposed()

    private fun startTicker() {
        tickerDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribe { tick: Long -> transition(Tick(tick)) }
    }

    private fun stopTicker() = tickerDisposable.dispose()

}