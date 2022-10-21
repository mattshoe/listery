package com.listery.ui.recipes.create.recyclerview

import com.listery.databinding.IngredientListItemBinding
import com.listery.ui.recyclerview.BaseViewHolder

class IngredientViewHolder(
    private val binding: IngredientListItemBinding
): BaseViewHolder(binding.root) {
    val name = binding.ingredientName
    val quantity = binding.ingredientQty
    val unit = binding.ingredientQtyUnit
    
    override fun recycle() {
        unit.text = null
        name.text = null
        quantity.text = null
    }
}