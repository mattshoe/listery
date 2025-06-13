package org.mattshoe.shoebox.listery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        RecipeIngredientCrossRef::class,
        RecipeStepEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ListeryDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeIngredientDao(): RecipeIngredientDao
    abstract fun recipeStepDao(): RecipeStepDao
} 