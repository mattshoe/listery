package com.listery.data.room.entities.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient")
data class IngredientEntity(
    @PrimaryKey
    val name: String
)