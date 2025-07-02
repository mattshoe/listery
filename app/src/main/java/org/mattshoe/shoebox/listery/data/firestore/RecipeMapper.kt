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
    val notes: String? = null,
    val steps: List<RecipeStepFirestoreModel> = emptyList()
)