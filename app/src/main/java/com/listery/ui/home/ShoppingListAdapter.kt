package com.listery.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.listery.data.room.entities.shoppinglist.ListItemEntity
import com.listery.databinding.ShoppingListItemBinding
import com.listery.ui.recyclerview.BaseAdapter
import javax.inject.Inject

class ShoppingListAdapter @Inject constructor(
): BaseAdapter<ListItemEntity, ShoppingListItemViewHolder>() {

    companion object {
        private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        val binding = ShoppingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShoppingListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        setText(holder.title, data[position].title)
        setText(holder.description, data[position].subtitle)
        setText(holder.quantity, data[position].quantity.toString())

        holder.checkbox.setOnClickListener {
            remove(position)
        }
    }

    override fun onViewRecycled(holder: ShoppingListItemViewHolder) {
        super.onViewRecycled(holder)
        holder.reset()
    }

    private fun randomText(maxChars: Int): String {
        val maxStart = maxChars - 1
        val randomStart = (0..maxStart).random()
        val randomEnd = (randomStart..maxChars).random()

        return LOREM_IPSUM.substring(randomStart, randomEnd)
    }

}

