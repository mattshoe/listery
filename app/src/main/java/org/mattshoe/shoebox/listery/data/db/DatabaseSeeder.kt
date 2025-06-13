package org.mattshoe.shoebox.listery.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mattshoe.shoebox.listery.data.TEMPORARY_RECIPES_STUBS
import org.mattshoe.shoebox.listery.data.toEntity
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val context: Context
) {
    fun createInitialDatabase(): ListeryDatabase {
        return Room.databaseBuilder(
            context,
            ListeryDatabase::class.java,
            "listery.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Create a new database instance for seeding
                val seedingDb = Room.databaseBuilder(
                    context,
                    ListeryDatabase::class.java,
                    "listery.db"
                ).build()

                runBlocking {
                    launch(Dispatchers.IO) {
                        // Insert all recipes
                        TEMPORARY_RECIPES_STUBS.forEach { recipe ->
                            val recipeId = seedingDb.recipeDao().insertRecipe(recipe.toEntity())

                            // Insert ingredients
                            recipe.ingredients.forEach { ingredient ->
                                val ingredientId = seedingDb.ingredientDao().getOrCreateIngredient(ingredient.name)
                                seedingDb.recipeIngredientDao().insertRecipeIngredient(
                                    RecipeIngredientCrossRef(
                                        recipeId = recipeId,
                                        ingredientId = ingredientId,
                                        quantity = ingredient.qty,
                                        unit = ingredient.unit
                                    )
                                )
                            }

                            // Insert steps
                            recipe.steps.forEachIndexed { index, step ->
                                seedingDb.recipeStepDao().insertStep(
                                    RecipeStepEntity(
                                        recipeId = recipeId,
                                        stepNumber = index + 1,
                                        instructions = step.instructions
                                    )
                                )
                            }
                        }
                        // Close the seeding database
                        seedingDb.close()
                    }
                }
            }
        }).build()
    }
} 