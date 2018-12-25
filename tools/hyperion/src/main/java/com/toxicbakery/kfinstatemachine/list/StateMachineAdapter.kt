package com.toxicbakery.kfinstatemachine.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.toxicbakery.kfinstatemachine.IStateMachine
import com.toxicbakery.kfinstatemachine.R

class StateMachineAdapter(
        private val machines: List<Pair<String, IStateMachine<*>>>
) : RecyclerView.Adapter<StateMachineHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateMachineHolder =
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.kfin_state_machine_view_holder_row, parent, false)
                    .let(::StateMachineHolder)

    override fun getItemCount(): Int = machines.size

    override fun onBindViewHolder(holder: StateMachineHolder, position: Int) =
            machines[position]
                    .let { (id, machine) ->
                        holder.bind(
                                id = id,
                                state = machine.state!!,
                                transitions = machine.transitions.joinToString(transform = { it.simpleName!! }))
                    }

}