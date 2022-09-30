package com.listery.data.room

import android.app.Application
import androidx.room.Room
import com.listery.data.room.entities.MeasurementUnitEntity
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import com.listery.data.room.entities.shoppinglist.ListItemEntity
import com.listery.data.room.entities.shoppinglist.ShoppingListEntity
import io.reactivex.Single
import kotlinx.coroutines.*

object DbInitializer {

    private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

    fun initialize(application: Application): AppDatabase {
        return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                "app-database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun seed(db: AppDatabase) {
        runBlocking {
            async(context = Dispatchers.IO) {
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

                    db.shoppingListDao().apply {

                        val shoppingLists = mutableListOf<ShoppingListEntity>().apply {
                            add(ShoppingListEntity("Shopping"))
                            add(ShoppingListEntity("Bonemantha"))
                            add(ShoppingListEntity("Strippers"))
                            add(ShoppingListEntity("Murduh"))
                            add(ShoppingListEntity("Halloween"))
                        }

                        val listItems = mutableListOf<ListItemEntity>().apply {
                            shoppingLists.forEach { list ->
                                randomSubset(LOREM_IPSUM.split(" ").toList()).forEach {
                                    add(
                                        ListItemEntity(
                                            list.name, it,
                                            it + "subtitle",
                                            (0..43214).random().toDouble(),
                                            units.shuffled().first().name,
                                            false
                                        )
                                    )

                                }
                            }
                        }

                        insertRecipeEntities(*recipeEntities.toTypedArray()).flatMap {
                            insertIngredientEntities(*ingredientEntities.toTypedArray()).flatMap {
                                insertMeasurementUnit(*units.toTypedArray()).flatMap {
                                    insertRecipeIngredientEntities(*recipeIngredientEntities.toTypedArray()).flatMap {
                                        insertShoppingListEntities(*shoppingLists.toTypedArray()).flatMap {
                                            insertListItemEntities(*listItems.toTypedArray()).map {
                                                it.map {
                                                    it + 1
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }.subscribe()
                    }
                }
            }
        }
    }



    fun <T> randomSubset(list: List<T>): List<T> {
        val start = list.indices.random()
        val end = (start..list.size).random()
        val shuffledList = list.shuffled()

        return list.subList(0, list.indices.random())
    }
}