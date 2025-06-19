package org.mattshoe.shoebox.listery.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeIngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredient(crossRef: RecipeIngredientCrossRef)

    @Delete
    suspend fun deleteRecipeIngredient(crossRef: RecipeIngredientCrossRef)

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun deleteAllIngredientsForRecipe(recipeId: Long)

    @Query("""
        SELECT i.*, ri.quantity, ri.unit 
        FROM ingredients i
        INNER JOIN recipe_ingredients ri ON i.id = ri.ingredientId
        WHERE ri.recipeId = :recipeId
        ORDER BY i.name COLLATE NOCASE
    """)
    suspend fun getIngredientsForRecipe(recipeId: Long): List<IngredientWithQuantity>

    @Transaction
    suspend fun updateRecipeIngredients(recipeId: Long, ingredients: List<RecipeIngredientCrossRef>) {
        deleteAllIngredientsForRecipe(recipeId)
        ingredients.forEach { insertRecipeIngredient(it) }
    }
}

data class IngredientWithQuantity(
    val id: Long,
    val name: String,
    val quantity: Float,
    val unit: String
) 