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
import com.toxicbakery.kfinstatemachine.FiniteState
import com.toxicbakery.kfinstatemachine.RulesBasedStateMachine

class MainActivity : AppCompatActivity(), StateListener<ViewState> {

    private val rootView: ConstraintLayout by lazy { findViewById<ConstraintLayout>(R.id.root_view) }
    private val contraintSetDefault: ConstraintSet = ConstraintSet()
    private val contraintSetAlt: ConstraintSet = ConstraintSet()

    private lateinit var viewStateMachine: ViewStateMachine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val initialState: ViewState = savedInstanceState
                ?.getString(EXTRA_VIEW_STATE, null)
                ?.let { stateName -> valueOf(stateName) }
                ?: DEFAULT

        viewStateMachine = ViewStateMachine(initialState, this)

        contraintSetDefault.clone(rootView)
        contraintSetAlt.clone(this, R.layout.activity_main_alt)
        rootView.setOnClickListener { viewStateMachine.transition(Animate) }
        onNewState(viewStateMachine.state)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_VIEW_STATE, viewStateMachine.state.name)
    }

    override fun onNewState(state: ViewState) {
        when (state) {
            ViewState.DEFAULT -> contraintSetDefault.applyTo(rootView)
            ViewState.ALTERNATE -> contraintSetAlt.applyTo(rootView)
        }

        val changeBounds = ChangeBounds().apply { interpolator = AccelerateDecelerateInterpolator() }
        TransitionManager.beginDelayedTransition(rootView, changeBounds)
    }

    companion object {
        private const val EXTRA_VIEW_STATE = "EXTRA_VIEW_STATE"
    }
}

interface StateListener<in F : FiniteState> {
    fun onNewState(state: F)
}

class ViewStateMachine(
        initialState: ViewState,
        private val stateListener: StateListener<ViewState>
) : RulesBasedStateMachine<ViewState>(
        initialState,
        transition(DEFAULT, ViewActions::class, ALTERNATE),
        transition(ALTERNATE, ViewActions::class, DEFAULT)
) {

    override fun transition(transition: Any) {
        super.transition(transition)
        stateListener.onNewState(state)
    }

}

sealed class ViewActions {
    object Animate : ViewActions()
}

enum class ViewState : FiniteState {
    DEFAULT,
    ALTERNATE;

    override val id: String
        get() = name
}