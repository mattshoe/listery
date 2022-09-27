package com.listery.data.room

import androidx.room.*

data class Recipe(
    @Embedded
    val details: RecipeMetadata,

    @Relation(
        parentColumn = "recipeName",
        entityColumn = "recipeName",
        entity = Ingredient::class
    )
    val ingredients: List<Ingredient>
)

@Entity
data class RecipeMetadata(
    @PrimaryKey
    @ColumnInfo(name = "recipeName")
    val name: String,

    @ColumnInfo(name = "notes")
    val notes: String
)

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

@Entity
data class IngredientMetadata(
    @PrimaryKey
    @ColumnInfo(name = "ingredientName")
    val name: String
)

@Entity
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @Embedded()
    val unit: MeasurementUnit,

    @ColumnInfo(name = "measurementQuantity")
    val quantity: Double
)

@Entity
data class MeasurementUnit(
    @PrimaryKey
    @ColumnInfo(name = "measurementUnit")
    val measurementUnit: String
)