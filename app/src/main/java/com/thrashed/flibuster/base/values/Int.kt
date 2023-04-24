package com.thrashed.flibuster.base.values

import android.content.res.Resources.getSystem

val Int.dp: Int get() = (this * getSystem().displayMetrics.density).toInt()