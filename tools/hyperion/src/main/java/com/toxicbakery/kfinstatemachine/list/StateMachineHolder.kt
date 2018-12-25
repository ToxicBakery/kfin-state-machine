package com.toxicbakery.kfinstatemachine.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.toxicbakery.kfinstatemachine.R

/**
 * Display the state of the current machine
 */
class StateMachineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textViewId: TextView by bind(R.id.kfin_state_machine_holder_id)
    private val textViewState: TextView by bind(R.id.kfin_state_machine_holder_state)
    private val textViewTransitons: TextView by bind(R.id.kfin_state_machine_holder_transitions)

    fun bind(
            id: String,
            state: Any,
            transitions: String
    ) {
        textViewId.text = id
        textViewState.text = state.toString()
        textViewTransitons.text = transitions
    }

}