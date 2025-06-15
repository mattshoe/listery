package org.mattshoe.shoebox.listery.data.db

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mattshoe.shoebox.listery.data.TEMPORARY_RECIPES_STUBS
import org.mattshoe.shoebox.listery.data.toEntity
import org.mattshoe.shoebox.listery.logging.log
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "listery_prefs"
        private const val KEY_DATABASE_SEEDED = "database_seeded"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun createInitialDatabase(): ListeryDatabase {
        return Room.databaseBuilder(
            context,
            ListeryDatabase::class.java,
            "listery.db"
        ).build()
            .also { database ->
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        // Only seed if database hasn't been seeded before
                        if (!prefs.getBoolean(KEY_DATABASE_SEEDED, false)) {
                            log("Seeding database for first time")
                            // Insert all recipes
                            TEMPORARY_RECIPES_STUBS.forEach { recipe ->
                                val recipeExists = database.recipeDao().recipeExists(recipe.name)
                                if (!recipeExists) {
                                    val recipeId = database.recipeDao().insertRecipe(recipe.toEntity())

                                    // Insert ingredients
                                    recipe.ingredients.forEach { ingredient ->
                                        val ingredientId =
                                            database.ingredientDao().getOrCreateIngredient(ingredient.name)
                                        database.recipeIngredientDao().insertRecipeIngredient(
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
                                        database.recipeStepDao().insertStep(
                                            RecipeStepEntity(
                                                recipeId = recipeId,
                                                stepNumber = index + 1,
                                                instructions = step.instructions
                                            )
                                        )
                                    }
                                } else {
                                    loge("recipe already exists! ${recipe.name}")
                                }
                            }
                            // Mark database as seeded
                            prefs.edit().putBoolean(KEY_DATABASE_SEEDED, true).apply()
                            log("Database seeding completed")
                        } else {
                            log("Database already seeded, skipping")
                        }
                    } catch (e: Throwable) {
                        loge("Error seeding database", e)
                        throw e // Re-throw to ensure the database creation fails if seeding fails
                    }
                }
            }
    }
}