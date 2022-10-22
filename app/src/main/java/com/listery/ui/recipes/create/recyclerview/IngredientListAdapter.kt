package com.listery.ui.recipes.create.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import com.listery.data.model.UserIngredient
import com.listery.databinding.IngredientListItemBinding
import com.listery.text.NumberFormatter
import com.listery.ui.recyclerview.BaseAdapter
import javax.inject.Inject

class IngredientListAdapter @Inject constructor(
    viewModel: IngredientListViewModel,
    private val numberFormatter: NumberFormatter
): BaseAdapter<UserIngredient, IngredientViewHolder, IngredientListViewModel>(viewModel) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = IngredientListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        setText(holder.name, data[position].entity.name)
        setText(
            holder.quantity,
            numberFormatter.concise(
                data[position].qty
            )
        )
        setText(holder.unit, data[position].unit?.name)
    }

}

