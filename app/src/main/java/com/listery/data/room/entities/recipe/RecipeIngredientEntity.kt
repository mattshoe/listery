package com.listery.data.room.entities.recipe

import androidx.room.Entity
import androidx.room.ForeignKey
import com.listery.data.model.UserIngredient
import com.listery.data.room.entities.MeasurementUnitEntity


@Entity(
    tableName = "recipe_ingredient",
    primaryKeys = [
        "recipeName",
        "ingredientName"
    ],
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["name"],
            childColumns = ["recipeName"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = ["name"],
            childColumns = ["ingredientName"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MeasurementUnitEntity::class,
            parentColumns = ["name"],
            childColumns = ["unit"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.SET_NULL
        )
    ]
)
data class RecipeIngredientEntity(
    val recipeName: String,
    val ingredientName: String,
    val quantity: Double,
    val unit: String?
) {
    constructor(
        recipeName: String,
        userIngredient: UserIngredient
    ) : this(
        recipeName,
        userIngredient.entity.name,
        userIngredient.qty,
        userIngredient.unit?.name
    )
}


