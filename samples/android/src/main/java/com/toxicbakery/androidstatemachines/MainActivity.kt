package com.toxicbakery.androidstatemachines

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import com.toxicbakery.androidstatemachines.ViewActions.Animate
import com.toxicbakery.androidstatemachines.ViewState.*
import com.toxicbakery.kfinstatemachine.KfinPlugin
import com.toxicbakery.kfinstatemachine.RxStateMachine
import com.toxicbakery.kfinstatemachine.StateMachine
import com.toxicbakery.kfinstatemachine.StateMachine.Companion.transition
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

class MainActivity : AppCompatActivity() {

    private val rootView: ConstraintLayout by lazy { findViewById<ConstraintLayout>(R.id.root_view) }
    private val constraintSetDefault: ConstraintSet = ConstraintSet()
    private val constraintSetAlt: ConstraintSet = ConstraintSet()

    private lateinit var viewStateMachine: ViewStateMachine
    private var viewStateDisposable: Disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val initialState: ViewState = savedInstanceState
                ?.getString(EXTRA_VIEW_STATE, null)
                ?.let { stateName -> valueOf(stateName) }
                ?: DEFAULT

        viewStateMachine = ViewStateMachine(initialState)
        viewStateDisposable = viewStateMachine.observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onNewState)

        constraintSetDefault.clone(rootView)
        constraintSetAlt.clone(this, R.layout.activity_main_alt)
        rootView.setOnClickListener { viewStateMachine.transition(Animate) }
        onNewState(viewStateMachine.state)

        KfinPlugin.registerMachine(MACHINE_VIEW_STATE, viewStateMachine)
        KfinPlugin.registerMachine("2", viewStateMachine)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_VIEW_STATE, viewStateMachine.state.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewStateDisposable.dispose()
        KfinPlugin.unregisterMachine(MACHINE_VIEW_STATE)
    }

    private fun onNewState(state: ViewState) {
        when (state) {
            ViewState.DEFAULT -> constraintSetDefault.applyTo(rootView)
            ViewState.ALTERNATE -> constraintSetAlt.applyTo(rootView)
        }

        val changeBounds = ChangeBounds().apply { interpolator = AccelerateDecelerateInterpolator() }
        TransitionManager.beginDelayedTransition(rootView, changeBounds)
    }

    companion object {
        private const val EXTRA_VIEW_STATE = "EXTRA_VIEW_STATE"
        private const val MACHINE_VIEW_STATE = "ViewStateMachine"
    }
}

class ViewStateMachine(
        initialState: ViewState
) : RxStateMachine<ViewState>(
        StateMachine(
                initialState,
                transition(DEFAULT, Animate::class, ALTERNATE),
                transition(ALTERNATE, Animate::class, DEFAULT)
        )
)

sealed class ViewActions {
    object Animate : ViewActions()
}

enum class ViewState {
    DEFAULT,
    ALTERNATE
}