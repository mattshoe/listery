package com.listery.ui.recipes.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.listery.data.model.UserRecipe
import com.listery.data.repository.IRecipeRepository
import com.listery.databinding.RecipeListItemBinding
import com.listery.text.NumberFormatter
import com.listery.ui.recipes.home.RecipesHomeFragmentDirections
import com.listery.ui.recyclerview.BaseAdapter
import javax.inject.Inject

class RecipeListAdapter @Inject constructor(
    viewModel: RecipeListViewModel,
    private val numberFormatter: NumberFormatter
): BaseAdapter<UserRecipe, RecipeViewHolder, RecipeListViewModel>(viewModel) {

    companion object {
        private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        setText(holder.title, data[position].entity.name)
        setText(holder.description, data[position].userIngredients.joinToString { it.entity.name }.trim())

        holder.root.setOnClickListener {
            it.findNavController().navigate(
                RecipesHomeFragmentDirections.actionNavigationRecipesHomeToNavigationRecipeCreate(holder.title.text.toString())
            )
        }
    }

    override fun onViewRecycled(holder: RecipeViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    private fun randomText(maxChars: Int): String {
        val maxStart = maxChars - 1
        val randomStart = (0..maxStart).random()
        val randomEnd = (randomStart..maxChars).random()

        return LOREM_IPSUM.substring(randomStart, randomEnd)
    }

}

