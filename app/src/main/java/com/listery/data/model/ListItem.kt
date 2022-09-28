package com.listery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ShoppingList(
    @PrimaryKey
    val name: String
)


@Entity
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long
)