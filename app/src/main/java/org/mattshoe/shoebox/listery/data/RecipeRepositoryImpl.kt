package org.mattshoe.shoebox.listery.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.mattshoe.shoebox.listery.data.db.IngredientDao
import org.mattshoe.shoebox.listery.data.db.RecipeDao
import org.mattshoe.shoebox.listery.data.db.RecipeEntity
import org.mattshoe.shoebox.listery.data.db.RecipeIngredientDao
import org.mattshoe.shoebox.listery.data.db.RecipeIngredientCrossRef
import org.mattshoe.shoebox.listery.data.db.RecipeStepDao
import org.mattshoe.shoebox.listery.data.db.RecipeStepEntity
import org.mattshoe.shoebox.listery.model.Ingredient
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.model.RecipeStep
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val recipeIngredientDao: RecipeIngredientDao,
    private val recipeStepDao: RecipeStepDao
) : RecipeRepository {
    override val recipes: Flow<List<Recipe>> = recipeDao.getAllRecipes().map { entities ->
        entities.map { it.toRecipe() }
    }

    override suspend fun fetch() {
        // No-op as we're using Room's Flow
    }

    override suspend fun fetch(name: String): Recipe? {
        val recipeEntity = recipeDao.getRecipeByName(name) ?: return null
        return recipeEntity.toRecipe()
    }

    override suspend fun upsert(recipe: Recipe) {
        val recipeId = recipeDao.upsertRecipe(recipe.toEntity())
        
        // Update ingredients
        val ingredientCrossRefs = recipe.ingredients.map { ingredient ->
            val ingredientId = ingredientDao.getOrCreateIngredient(ingredient.name)
            RecipeIngredientCrossRef(
                recipeId = recipeId,
                ingredientId = ingredientId,
                quantity = ingredient.qty,
                unit = ingredient.unit
            )
        }
        recipeIngredientDao.updateRecipeIngredients(recipeId, ingredientCrossRefs)

        // Update steps
        val stepEntities = recipe.steps.mapIndexed { index, step ->
            RecipeStepEntity(
                recipeId = recipeId,
                stepNumber = index + 1,
                instructions = step.instructions
            )
        }
        recipeStepDao.updateRecipeSteps(recipeId, stepEntities)
    }

    override suspend fun remove(name: String) {
        recipeDao.deleteRecipeByName(name)
    }

    override suspend fun exists(name: String): Boolean {
        return recipeDao.recipeExists(name)
    }

    override fun observe(name: String): Flow<Recipe?> {
        return recipes.map { recipeList ->
            recipeList.firstOrNull { it.name == name }
        }.distinctUntilChanged()
    }

    private suspend fun RecipeEntity.toRecipe(): Recipe {
        val ingredients = recipeIngredientDao.getIngredientsForRecipe(id).map { 
            Ingredient(
                name = it.name,
                qty = it.quantity,
                unit = it.unit
            )
        }
        val steps = recipeStepDao.getStepsForRecipe(id).map {
            RecipeStep(instructions = it.instructions)
        }
        return Recipe(
            name = name,
            starred = starred,
            url = url,
            calories = calories,
            ingredients = ingredients,
            prepTime = prepTimeMinutes?.minutes,
            notes = notes,
            steps = steps
        )
    }

}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        name = name,
        starred = starred,
        url = url,
        calories = calories,
        prepTimeMinutes = prepTime?.inWholeMinutes?.toInt(),
        notes = notes
    )
}
