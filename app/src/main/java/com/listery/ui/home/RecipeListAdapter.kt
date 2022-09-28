package com.listery.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.listery.data.repository.IRecipeRepository
import com.listery.data.model.recipe.Recipe
import com.listery.databinding.RecipeListItemBinding
import com.listery.ui.recyclerview.BaseAdapter
import javax.inject.Inject

class RecipeListAdapter @Inject constructor(
    private val recipeRepository: IRecipeRepository
): BaseAdapter<Recipe, RecipeViewHolder>() {

    companion object {
        private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        setText(holder.title, randomText(75))
        setText(holder.description, randomText(LOREM_IPSUM.length - 1))
        setText(holder.quantity, (0..1000).random().toString())

        holder.checkbox.setOnClickListener {
            remove(position)
            recipeRepository.getRecipes()
        }
    }

    override fun onViewRecycled(holder: RecipeViewHolder) {
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

