package com.toxicbakery.kfinstatemachine.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.toxicbakery.kfinstatemachine.R
import com.toxicbakery.kfinstatemachine.registeredMachines

class StateMachineListActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by bind(R.id.kfin_recycler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kfin_activity_list)
        setSupportActionBar(findViewById(R.id.kfin_toolbar))
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(true) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, VERTICAL))
        recyclerView.adapter = StateMachineAdapter(registeredMachines)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}