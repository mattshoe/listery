package org.mattshoe.shoebox.listery.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.model.Recipe
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(

): RecipeRepository {
    val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    override val recipes: Flow<List<Recipe>> = _recipes

    override suspend fun fetch() {
        _recipes.emit(
            listOf(
                Recipe(
                    "Avocado toast and eggs",
                    false,
                    null,
                    275,
                    5,
                    "20 min"
                ),
                Recipe(
                    "Poop on a stick",
                    false,
                    "https://www.poopstick.com",
                    275,
                    5,
                    "45 min"
                ),
                Recipe(
                    "Kraft mac n cheese",
                    true,
                    null,
                    400,
                    2,
                    "20 min"
                ),
                Recipe(
                    "Cauliflower Pizza",
                    true,
                    "https://www.google.com",
                    700,
                    1,
                    "20 min"
                ),
                Recipe(
                    "Papa John's",
                    false,
                    "https://www.yourmom.com",
                    1250,
                    1,
                    "1 hr"
                ),
                Recipe(
                    "Butter Chicken",
                    false,
                    "https://www.butterchicken.com",
                    650,
                    24,
                    "1 hr 10 min"
                ),
                Recipe(
                    "Sunday WingDay",
                    true,
                    "https://www.sundayfunday.com",
                    400,
                    3,
                    "35 min"
                ),
                Recipe(
                    "Spaghetti O's",
                    false,
                    "https://www.google.com",
                    200,
                    1,
                    "5 min"
                ),
                Recipe(
                    "Fish and Chips",
                    true,
                    "https://www.reducetarian.com",
                    525,
                    8,
                    "45 min"
                )
            )
        )
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

    fun <T> List<T>.removeFirst(predicate: (T) -> Boolean): List<T> {
        val index = indexOfFirst(predicate)
        return if (index == -1) this else toMutableList().also { it.removeAt(index) }
    }
}