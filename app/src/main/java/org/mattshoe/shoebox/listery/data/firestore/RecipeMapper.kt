package org.mattshoe.shoebox.listery.data.firestore

import org.mattshoe.shoebox.listery.model.Recipe
import kotlin.time.Duration.Companion.seconds

fun Recipe.toFirestoreModel(): FirestoreRecipeModel {
    return FirestoreRecipeModel(
        id,
        name,
        starred,
        url,
        calories,
        ingredients.map { it.id },
        prepTime?.inWholeSeconds,
        servings,
        notes,
        steps.map { it.toFirestoreModel() }
    )
}

fun FirestoreRecipeModel.toRecipe(ingredientsMap: Map<String, IngredientFirestoreModel>): Recipe {
    return Recipe(
        id,
        name,
        starred,
        url,
        ingredients.mapNotNull { ingredientsMap[it]?.toIngredient() },
        prepTime?.seconds,
        servings,
        notes,
        steps.map { it.toRecipeStep() }
    )
}

data class FirestoreRecipeModel(
    val id: String = "",
    val name: String = "",
    val starred: Boolean = false,
    val url: String? = null,
    val calories: Int? = null,
    val ingredients: List<String> = emptyList(),
    val prepTime: Long? = null,
    val servings: Int = 1,
    val notes: String? = null,
    val steps: List<RecipeStepFirestoreModel> = emptyList()
)