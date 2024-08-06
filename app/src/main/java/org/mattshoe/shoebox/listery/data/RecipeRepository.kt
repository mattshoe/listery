package org.mattshoe.shoebox.listery.data

import kotlinx.coroutines.flow.Flow
import org.mattshoe.shoebox.listery.model.Recipe

interface RecipeRepository {
    val recipes: Flow<List<Recipe>>
    suspend fun fetch()
}


