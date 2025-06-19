package org.mattshoe.shoebox.listery.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.data.db.IngredientDao
import org.mattshoe.shoebox.listery.data.db.IngredientEntity
import org.mattshoe.shoebox.listery.data.db.ListeryDatabase
import org.mattshoe.shoebox.listery.data.db.RecipeDao
import org.mattshoe.shoebox.listery.data.db.RecipeOverviewEntity
import org.mattshoe.shoebox.listery.data.db.RecipeIngredientDao
import org.mattshoe.shoebox.listery.data.db.RecipeIngredientCrossRef
import org.mattshoe.shoebox.listery.data.db.RecipeStepDao
import org.mattshoe.shoebox.listery.data.db.RecipeStepEntity
import org.mattshoe.shoebox.listery.logging.log
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.model.Ingredient
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.model.RecipeStep
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class RecipeRepositoryImpl @Inject constructor(
    private val db: ListeryDatabase,
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val recipeIngredientDao: RecipeIngredientDao,
    private val recipeStepDao: RecipeStepDao
) : RecipeRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())

    override val recipes: Flow<List<Recipe>> = _recipes.asStateFlow()

    init {
        recipes.onEach {
            log("RecipeRepository.recipes updated with ${it.count()} recipes")
        }.launchIn(scope)

        recipeDao.getAllRecipes()
            .map { entities ->
                log("List of recipes updated!! ${entities.count()} recipes now.")
                entities.map {
                    it.toRecipe()
                }
            }
            .onEach { recipes ->
                log("Emitting ${recipes.count()} recipes to RecipeRepository.recipes")
                _recipes.update {
                    recipes
                }
            }
            .catch {
                loge("Error in recipeDao.getAllRecipes()", it)
            }
            .launchIn(scope)
    }

    override suspend fun fetch(name: String): Recipe? {
        val recipeEntity = recipeDao.getRecipeByName(name) ?: return null
        return recipeEntity.toRecipe()
    }

    override suspend fun upsert(recipe: Recipe) {
        db.withTransaction {
            val recipeId = recipeDao.upsertRecipeOverview(recipe.toOverviewEntity())

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

    }

    override suspend fun remove(name: String) {
        db.withTransaction {
            recipeDao.getRecipeByName(name)?.let { recipe ->
                recipeDao.deleteRecipeOverviewByName(recipe.overview.name)
                recipeIngredientDao.updateRecipeIngredients(recipe.overview.id, emptyList())
            }
        }
    }

    override suspend fun exists(name: String): Boolean {
        return recipeDao.recipeExists(name)
    }

    override fun observe(name: String): Flow<Recipe?> {
        return recipes.map { recipeList ->
            recipeList.firstOrNull { it.name == name }
        }
    }

    private suspend fun RecipeEntity.toRecipe(): Recipe {
        val ingredients = recipeIngredientDao.getIngredientsForRecipe(overview.id).map {
            Ingredient(
                name = it.name,
                qty = it.quantity,
                unit = it.unit
            )
        }
        val steps = recipeStepDao.getStepsForRecipe(overview.id).map {
            RecipeStep(instructions = it.instructions)
        }
        return Recipe(
            name = overview.name,
            starred = overview.starred,
            url = overview.url,
            calories = overview.calories,
            ingredients = ingredients,
            prepTime = overview.prepTimeMinutes?.minutes,
            notes = overview.notes,
            steps = steps
        )
    }

}

fun Recipe.toOverviewEntity(): RecipeOverviewEntity {
    return RecipeOverviewEntity(
        name = name,
        starred = starred,
        url = url,
        calories = calories,
        prepTimeMinutes = prepTime?.inWholeMinutes?.toInt(),
        notes = notes
    )
}

data class RecipeEntity(
    @Embedded val overview: RecipeOverviewEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RecipeIngredientCrossRef::class,
            parentColumn = "recipeId",
            entityColumn = "ingredientId"
        )
    )
    val ingredients: List<IngredientEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val steps: List<RecipeStepEntity>
)
