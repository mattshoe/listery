package com.listery.data.model.recipe

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ingredientId")
    val id: Long = 0,

    @ColumnInfo(name = "recipeName")
    val recipeName: String,

    @Embedded
    val details: IngredientMetadata,

    @Embedded
    val measurement: Measurement
)