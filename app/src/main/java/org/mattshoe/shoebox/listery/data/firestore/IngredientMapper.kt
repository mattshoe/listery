package org.mattshoe.shoebox.listery.data.firestore

import org.mattshoe.shoebox.listery.model.Ingredient

fun Ingredient.toFirestoreModel(): IngredientFirestoreModel {
    return IngredientFirestoreModel(
        name,
        qty,
        unit
    )
}

fun IngredientFirestoreModel.toIngredient(): Ingredient {
    return Ingredient(
        name,
        qty,
        unit
    )
}

data class IngredientFirestoreModel(
    val name: String = "",
    val qty: Float = 0f,
    val unit: String = ""
)