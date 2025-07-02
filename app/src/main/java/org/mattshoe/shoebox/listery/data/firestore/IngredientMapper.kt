package org.mattshoe.shoebox.listery.data.firestore

import org.mattshoe.shoebox.listery.model.Ingredient
import java.util.UUID

fun Ingredient.toFirestoreModel(): IngredientFirestoreModel {
    return IngredientFirestoreModel(
        id,
        name,
        qty,
        unit,
        calories
    )
}

fun IngredientFirestoreModel.toIngredient(): Ingredient {
    return Ingredient(
        id,
        name,
        qty,
        unit,
        calories
    )
}

data class IngredientFirestoreModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val qty: Float = 0f,
    val unit: String = "",
    val calories: Int = 0
)