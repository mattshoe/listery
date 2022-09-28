package com.listery.data.model.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeMetadata(
    @PrimaryKey
    @ColumnInfo(name = "recipeName")
    val name: String,

    @ColumnInfo(name = "notes")
    val notes: String
)