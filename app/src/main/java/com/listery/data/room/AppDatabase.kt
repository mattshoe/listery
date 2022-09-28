package com.listery.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.listery.data.model.MeasurementUnit
import com.listery.data.model.recipe.*

@Database(
    entities = [
        RecipeMetadata::class,
        Ingredient::class,
        IngredientMetadata::class,
        Measurement::class,
        MeasurementUnit::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}