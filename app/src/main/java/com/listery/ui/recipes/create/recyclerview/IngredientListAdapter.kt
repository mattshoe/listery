package com.listery.ui.recipes.create.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import com.listery.data.model.UserIngredient
import com.listery.data.model.UserRecipe
import com.listery.data.repository.IRecipeRepository
import com.listery.databinding.IngredientListItemBinding
import com.listery.databinding.RecipeListItemBinding
import com.listery.ui.recyclerview.BaseAdapter
import javax.inject.Inject

class IngredientListAdapter @Inject constructor(
    viewModel: IngredientListViewModel
): BaseAdapter<UserIngredient, IngredientViewHolder, IngredientListViewModel>(viewModel) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = IngredientListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        setText(holder.name, data[position].entity.name)
        setText(holder.quantity, data[position].qty.toString())
        setText(holder.unit, data[position].unit?.name)
    }

}

