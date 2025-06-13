package org.mattshoe.shoebox.listery.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val starred: Boolean,
    val url: String?,
    val calories: Int?,
    val prepTimeMinutes: Int?,
    val notes: String?
) 