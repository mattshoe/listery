package com.listery.data.room.entities.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey
    val name: String,

    val notes: String
)