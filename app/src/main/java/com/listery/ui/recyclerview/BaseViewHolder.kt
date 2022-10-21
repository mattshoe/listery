package com.listery.ui.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {
    val root: View
        get() = view.rootView

    abstract fun recycle()
}