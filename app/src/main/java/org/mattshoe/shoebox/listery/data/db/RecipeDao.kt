package org.mattshoe.shoebox.listery.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE name = :name")
    suspend fun getRecipeByName(name: String): RecipeEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM recipes WHERE name = :name)")
    suspend fun recipeExists(name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes WHERE name = :name")
    suspend fun deleteRecipeByName(name: String)

    @Transaction
    suspend fun upsertRecipe(recipe: RecipeEntity): Long {
        val existingRecipe = getRecipeByName(recipe.name)
        return if (existingRecipe != null) {
            updateRecipe(recipe.copy(id = existingRecipe.id))
            existingRecipe.id
        } else {
            insertRecipe(recipe)
        }
    }
} 