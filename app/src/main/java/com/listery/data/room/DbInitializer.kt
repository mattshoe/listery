package com.listery.data.room

import android.app.Application
import androidx.room.Room
import com.listery.data.room.entities.MeasurementUnitEntity
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import io.reactivex.Single
import kotlinx.coroutines.*

object DbInitializer {

    fun initialize(application: Application): AppDatabase {
        return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                "app-database"
            )
            .fallbackToDestructiveMigration()
            .build().apply {
                runBlocking {
                    async(context = Dispatchers.IO) {
                        seed(this@apply)
                    }
                }
            }
    }

    fun seed(db: AppDatabase) {
        db.recipeDao().apply {
            val recipeEntities = mutableListOf<RecipeEntity>().apply {
                repeat(100) {
                    add(
                        RecipeEntity(
                            "Recipe #$it",
                            "notes for recipe $it"
                        )
                    )
                }
            }
            val ingredientEntities = mutableListOf<IngredientEntity>().apply {
                repeat(15) {
                    add(
                        IngredientEntity(name = "Ingredint $it")
                    )
                }
            }
            val units = mutableListOf<MeasurementUnitEntity>().apply {
                add(MeasurementUnitEntity("cups"))
                add(MeasurementUnitEntity("oz"))
                add(MeasurementUnitEntity("tbsp"))
                add(MeasurementUnitEntity("tsp"))
                add(MeasurementUnitEntity("pcs"))
                add(MeasurementUnitEntity("lbs"))
                add(MeasurementUnitEntity("gallons"))
            }
            val recipeIngredientEntities = mutableListOf<RecipeIngredientEntity>().apply {
                recipeEntities.forEach { recipe ->
                    randomSubset(ingredientEntities).forEach { ingredient ->
                        val qty = (1..434231).random().toDouble()
                        val unit = units[(0 until units.size).random()]

                        add(
                            RecipeIngredientEntity(
                                recipe.name,
                                ingredient.name,
                                qty,
                                unit.name
                            )
                        )
                    }


                }
            }

            insertRecipeEntities(*recipeEntities.toTypedArray()).flatMap {
                insertIngredientEntities(*ingredientEntities.toTypedArray()).flatMap {
                    insertMeasurementUnit(*units.toTypedArray()).flatMap {
                        insertRecipeIngredientEntities(*recipeIngredientEntities.toTypedArray())
                    }
                }
            }.subscribe()
        }

    }



    fun <T> randomSubset(list: List<T>): List<T> {
        val start = list.indices.random()
        val end = (start..list.size).random()
        val shuffledList = list.shuffled()

        return list.subList(0, list.indices.random())
    }
}