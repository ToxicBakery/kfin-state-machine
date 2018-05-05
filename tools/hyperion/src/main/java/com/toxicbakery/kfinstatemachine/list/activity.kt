package com.toxicbakery.kfinstatemachine.list

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

internal fun <T : View> Activity.bind(@IdRes id: Int) = lazy { findViewById<T>(id) }