package com.listery.di

import com.listery.ui.BaseViewModel
import com.listery.ui.recyclerview.BaseAdapter
import com.listery.ui.recyclerview.BaseViewHolder
import javax.inject.Provider

fun <TData, TAdapter: BaseAdapter<TData, *, *>> Provider<TAdapter>.get(data: List<TData>): TAdapter {
    return get().apply {
        init(data)
    }
}