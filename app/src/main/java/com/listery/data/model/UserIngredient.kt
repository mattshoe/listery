package com.listery.data.model

import com.listery.data.room.entities.MeasurementUnitEntity
import com.listery.data.room.entities.recipe.IngredientEntity

data class UserIngredient(
    val entity: IngredientEntity,
    val qty: Double,
    val unit: MeasurementUnitEntity?
)
