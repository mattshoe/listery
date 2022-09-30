package com.listery.data.room.entities.shoppinglist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListEntity(
    @PrimaryKey
    val name: String,
    val notes: String? = null
)