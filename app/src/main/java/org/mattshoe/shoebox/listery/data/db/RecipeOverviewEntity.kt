package org.mattshoe.shoebox.listery.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeOverviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val starred: Boolean,
    val url: String?,
    val calories: Int?,
    val prepTimeMinutes: Int?,
    val notes: String?
) 