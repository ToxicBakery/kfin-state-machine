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

class StopwatchMachine : StateMachine<TimerState, TimerEvent>(
        Stopped,
        transition(Stopped, Start::class, Running),
        transition(Running, Tick::class, Running),
        transition(Running, Stop::class, Stopped)
) {

    private var tickerDisposable: Disposable = Disposables.disposed()

    override fun transition(transition: TimerEvent) {
        super.transition(transition)
        when(transition) {
            is Start -> startTicker()
            is Stop -> stopTicker()
            is Tick -> println(transition.tick)
        }
    }

    private fun startTicker() {
        tickerDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribe { tick: Long -> transition(Tick(tick)) }
    }

    private fun stopTicker() = tickerDisposable.dispose()

}
