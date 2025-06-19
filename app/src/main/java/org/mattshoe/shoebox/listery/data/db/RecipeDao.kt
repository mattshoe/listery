package org.mattshoe.shoebox.listery.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.mattshoe.shoebox.listery.data.RecipeEntity

@Dao
interface RecipeDao {
    @Transaction
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE name = :name")
    suspend fun getRecipeByName(name: String): RecipeEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM recipes WHERE name = :name)")
    suspend fun recipeExists(name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeOverview(recipe: RecipeOverviewEntity): Long

    @Update
    suspend fun updateRecipeOverview(recipe: RecipeOverviewEntity)

    @Delete
    suspend fun deleteRecipeOverview(recipe: RecipeOverviewEntity)

    @Query("DELETE FROM recipes WHERE name = :name")
    suspend fun deleteRecipeOverviewByName(name: String)

    @Transaction
    suspend fun upsertRecipeOverview(recipe: RecipeOverviewEntity): Long {
        val existingRecipe = getRecipeByName(recipe.name)
        return if (existingRecipe != null) {
            updateRecipeOverview(recipe.copy(id = existingRecipe.overview.id))
            existingRecipe.overview.id
        } else {
            insertRecipeOverview(recipe)
        }
    }
} 