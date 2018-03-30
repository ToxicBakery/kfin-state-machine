package com.toxicbakery.sample.countdown

import com.toxicbakery.kfinstatemachine.BaseMachine
import com.toxicbakery.kfinstatemachine.FiniteState
import com.toxicbakery.kfinstatemachine.Transition
import com.toxicbakery.kfinstatemachine.graph.DirectedGraph
import com.toxicbakery.sample.countdown.CountdownMachine.TimerEvent
import com.toxicbakery.sample.countdown.CountdownMachine.TimerEvent.*
import com.toxicbakery.sample.countdown.CountdownMachine.TimerState
import com.toxicbakery.sample.countdown.CountdownMachine.TimerState.Running
import com.toxicbakery.sample.countdown.CountdownMachine.TimerState.Stopped
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class CountdownMachine : BaseMachine<TimerState, TimerEvent>(
        directedGraph = DirectedGraph(
                mappedEdges = mapOf(
                        Stopped to mapOf<TimerEvent, TimerState>(
                                Start to Running
                        ),
                        Running to mapOf(
                                Stop to Stopped,
                                Tick to Running
                        )
                )
        ),
        initialState = Stopped
) {

    private val subscriptions = CompositeDisposable()

    private val timerDisposable: Disposable
        get() = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe { performTransition(Tick) }

    init {
        addOnTransitionListener { (transition, targetState) ->
            println("Machine transitioning for ${transition.event} to ${targetState.id}")
            when (transition) {
                Start -> {
                    println("Creating timer")
                    subscriptions.add(timerDisposable)
                }
                Stop -> {
                    println("Stopping timer")
                    subscriptions.clear()
                }
            }
        }
        addOnStateChangeListener { timerState ->
            println("Machine moved to ${timerState.id}")
        }
    }

    sealed class TimerState : FiniteState {

        override val id: String
            get() = javaClass.simpleName

        object Stopped : TimerState()
        object Running : TimerState()
    }

    sealed class TimerEvent : Transition {

        override val event: String
            get() = javaClass.simpleName

        object Stop : TimerEvent()
        object Start : TimerEvent()
        object Tick : TimerEvent()
    }
}