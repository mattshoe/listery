package com.listery.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.listery.data.model.recipe.Ingredient
import com.listery.data.model.recipe.Recipe
import com.listery.data.model.recipe.RecipeMetadata

@Dao
interface ShoppingListDao {

    @Transaction
    @Query("SELECT * FROM recipemetadata")
    fun getAll(): LiveData<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeMetadata(vararg recipeMetadata: RecipeMetadata)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredients(vararg ingredient: Ingredient)

//    @Delete
//    abstract fun delete(recipe: RecipeWithIngredients)

}