package com.toxicbakery.kfinstatemachine

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toxicbakery.kfinstatemachine.list.StateMachineListActivity
import com.willowtreeapps.hyperion.plugin.v1.PluginModule

open class KfinPluginModule : PluginModule(), View.OnClickListener {

    override fun createPluginView(layoutInflater: LayoutInflater, parent: ViewGroup): View? =
            layoutInflater.inflate(R.layout.kfin_item_plugin, parent, false)
                    .apply { setOnClickListener(this@KfinPluginModule) }

    override fun onClick(v: View) =
            Intent(context, StateMachineListActivity::class.java)
                    .let(context::startActivity)

}