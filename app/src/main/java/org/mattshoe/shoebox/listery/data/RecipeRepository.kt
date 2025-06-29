package org.mattshoe.shoebox.listery.data

import kotlinx.coroutines.flow.Flow
import org.mattshoe.shoebox.listery.model.Recipe

interface RecipeRepository {
    val userRecipes: Flow<List<Recipe>>

    suspend fun fetch(id: String): Recipe?
    suspend fun upsert(recipe: Recipe): String
    suspend fun remove(id: String)
    suspend fun exists(id: String): Boolean

    fun observe(id: String): Flow<Recipe?>
}


