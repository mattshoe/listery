package com.listery.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.listery.data.room.Ingredient
import com.listery.data.room.Recipe
import com.listery.data.room.RecipeMetadata

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM recipemetadata")
    fun getAll(): LiveData<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRecipeMetadata(vararg recipeMetadata: RecipeMetadata)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIngredients(vararg ingredient: Ingredient)

//    @Transaction
//    @Query("SELECT * FROM recipe WHERE name LIKE :name LIMIT 1")
//    abstract fun getRecipe(name: String): RecipeWithIngredients
//
//    @Transaction
//    @Insert
//    abstract fun insertAll(vararg recipes: RecipeWithIngredients)

//    @Delete
//    abstract fun delete(recipe: RecipeWithIngredients)

}