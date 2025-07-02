package org.mattshoe.shoebox.listery.data.firestore

import org.mattshoe.shoebox.listery.model.RecipeStep
import java.util.UUID

fun RecipeStep.toFirestoreModel(): RecipeStepFirestoreModel {
    return RecipeStepFirestoreModel(
        instructions,
        id
    )
}

fun RecipeStepFirestoreModel.toRecipeStep(): RecipeStep {
    return RecipeStep(
        instructions,
        key
    )
}

data class RecipeStepFirestoreModel(
    val instructions: String = "",
    val key: String = UUID.randomUUID().toString()
)