package org.mattshoe.shoebox.listery.data

import kotlinx.coroutines.flow.Flow
import org.mattshoe.shoebox.listery.model.Recipe

interface RecipeRepository {
    val recipes: Flow<List<Recipe>>
    suspend fun fetch()
    suspend fun fetch(name: String): Recipe?
    suspend fun upsert(recipe: Recipe)
    suspend fun remove(name: String)
    suspend fun exists(name: String): Boolean
}


