package com.listery.ui.recipes.home.recyclerview

import com.listery.databinding.RecipeListItemBinding
import com.listery.ui.recyclerview.BaseViewHolder

class RecipeViewHolder(
    private val binding: RecipeListItemBinding
): BaseViewHolder(binding.root) {
    val title = binding.itemTitle
    val description = binding.description
    
    override fun recycle() {
        title.text = null
        description.text = null
    }
}