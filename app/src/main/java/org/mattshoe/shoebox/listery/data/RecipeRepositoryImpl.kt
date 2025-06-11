package org.mattshoe.shoebox.listery.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.model.Recipe
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(

): RecipeRepository {
    val _recipes = MutableStateFlow(TEMPORARY_RECIPES_STUBS)
    override val recipes: Flow<List<Recipe>> = _recipes

    override suspend fun fetch() {
        _recipes.update { TEMPORARY_RECIPES_STUBS }
    }

    override suspend fun fetch(name: String): Recipe? {
        return _recipes.value.firstOrNull {
            it.name == name
        }
    }

    override suspend fun upsert(recipe: Recipe) {
        _recipes.update { current ->
            current.upsert(recipe) {
                it.name == recipe.name
            }
        }
    }

    override suspend fun remove(name: String) {
        _recipes.update { current ->
            current.removeFirst {
                it.name == name
            }
        }
    }

    override suspend fun exists(name: String): Boolean {
        return _recipes.value.any { it.name == name }
    }

    override fun observe(name: String): Flow<Recipe?> {
        return recipes.map { recipeList ->
            recipeList.firstOrNull { recipe ->
                recipe.name == name
            }
        }.distinctUntilChanged()
    }

    private inline fun <T> List<T>.upsert(
        newItem: T,
        predicate: (T) -> Boolean
    ): List<T> {
        val index = indexOfFirst(predicate)
        return if (index != -1) {
            toMutableList().also { it[index] = newItem }
        } else {
            this + newItem
        }
    }

    private fun <T> List<T>.removeFirst(predicate: (T) -> Boolean): List<T> {
        val index = indexOfFirst(predicate)
        return if (index == -1) this else toMutableList().also { it.removeAt(index) }
    }
}
