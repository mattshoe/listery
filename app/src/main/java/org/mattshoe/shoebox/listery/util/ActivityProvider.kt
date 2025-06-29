package org.mattshoe.shoebox.listery.util

import android.annotation.SuppressLint
import android.app.Activity

@SuppressLint("StaticFieldLeak")
object ActivityProvider {
    var currentActivity: Activity? = null
}