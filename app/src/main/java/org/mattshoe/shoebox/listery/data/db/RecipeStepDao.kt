package org.mattshoe.shoebox.listery.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeStepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: RecipeStepEntity)

    @Delete
    suspend fun deleteStep(step: RecipeStepEntity)

    @Query("DELETE FROM recipe_steps WHERE recipeId = :recipeId")
    suspend fun deleteAllStepsForRecipe(recipeId: Long)

    @Query("SELECT * FROM recipe_steps WHERE recipeId = :recipeId ORDER BY stepNumber")
    suspend fun getStepsForRecipe(recipeId: Long): List<RecipeStepEntity>

    @Transaction
    suspend fun updateRecipeSteps(recipeId: Long, steps: List<RecipeStepEntity>) {
        deleteAllStepsForRecipe(recipeId)
        steps.forEach { insertStep(it) }
    }
} 