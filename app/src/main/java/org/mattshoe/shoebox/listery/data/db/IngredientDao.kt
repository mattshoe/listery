package org.mattshoe.shoebox.listery.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE name = :name")
    suspend fun getIngredientByName(name: String): IngredientEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIngredient(ingredient: IngredientEntity): Long

    @Transaction
    suspend fun getOrCreateIngredient(name: String): Long {
        val existing = getIngredientByName(name)
        return if (existing != null) {
            existing.id
        } else {
            insertIngredient(IngredientEntity(name = name))
        }
    }
} 