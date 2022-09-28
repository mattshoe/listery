package com.listery.data.model.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IngredientMetadata(
    @PrimaryKey
    @ColumnInfo(name = "ingredientName")
    val name: String
)