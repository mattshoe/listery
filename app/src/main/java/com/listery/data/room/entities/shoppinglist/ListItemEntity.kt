package com.listery.data.room.entities.shoppinglist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


@Entity(
    tableName = "list_item",
    foreignKeys = [
            ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("owningList"),
            onDelete = CASCADE
        )
    ]
)
data class ListItemEntity(
    val owningList: String,
    val title: String,
    val subtitle: String?,
    val quantity: Double?,
    val unit: String?,
    val complete: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)