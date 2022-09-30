package com.listery.data.model

import com.listery.data.room.entities.shoppinglist.ListItemEntity
import com.listery.data.room.entities.shoppinglist.ShoppingListEntity

data class UserShoppingList(
    val entity: ShoppingListEntity,
    val items: List<ListItemEntity>
)