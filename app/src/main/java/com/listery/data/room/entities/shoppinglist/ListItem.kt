package com.listery.data.room.entities.shoppinglist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


@Entity
data class ShoppingList(
    @PrimaryKey
    val name: String,
    val notes: String
)

@Entity(
    foreignKeys = [
            ForeignKey(
            entity = ShoppingList::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("owningList"),
            onDelete = CASCADE
        )
    ]
)
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val owningList: String,
    val title: String,
    val subtitle: String?,
    val quantity: Double?,
    val unit: String?,
    val complete: Boolean
)