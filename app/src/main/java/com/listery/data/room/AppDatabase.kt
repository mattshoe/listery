package com.listery.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.listery.data.room.entities.MeasurementUnitEntity
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import com.listery.data.room.entities.shoppinglist.ListItemEntity
import com.listery.data.room.entities.shoppinglist.ShoppingListEntity

@Database(
    entities = [
        // Recipe
        RecipeEntity::class,
        IngredientEntity::class,
        MeasurementUnitEntity::class,
        RecipeIngredientEntity::class,

        // Shopping list
        ShoppingListEntity::class,
        ListItemEntity::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun shoppingListDao(): ShoppingListDao
}