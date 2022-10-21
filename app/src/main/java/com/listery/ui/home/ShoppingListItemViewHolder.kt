package com.listery.ui.home

import com.listery.databinding.RecipeListItemBinding
import com.listery.databinding.ShoppingListItemBinding
import com.listery.ui.recyclerview.BaseViewHolder

class ShoppingListItemViewHolder(
    private val binding: ShoppingListItemBinding
): BaseViewHolder(binding.root) {
    val checkbox = binding.checkbox
    val title = binding.itemTitle
    val description = binding.description
    val quantity = binding.quantity
    
    override fun recycle() {
        title.text = null
        description.text = null
        quantity.text = null
        checkbox.setOnClickListener(null)
        checkbox.isChecked = false
    }
}