package com.listery.ui.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<TData, TViewHolder: BaseViewHolder>: RecyclerView.Adapter<TViewHolder>() {
    protected lateinit var data: MutableList<TData>

    open fun init(data: List<TData>) {
        this.data = data.toMutableList()
    }

    override fun getItemCount() = data.size

    protected fun remove(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }

    override fun onViewRecycled(holder: TViewHolder) {
        super.onViewRecycled(holder)
        holder.reset()
    }

    protected fun setText(view: TextView, text: CharSequence?) {
        text?.let { text ->
            if (text.length > 0) {
                view.setText(text)
                view.visibility = View.VISIBLE
            }
            else {
                view.visibility = View.GONE
            }
        } ?: run { view.visibility = View.GONE }

    }
}