package com.toxicbakery.kfinstatemachine.list

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

fun <T : View> RecyclerView.ViewHolder.bind(@IdRes id: Int) = lazy { itemView.findViewById<T>(id) }