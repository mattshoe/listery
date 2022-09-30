package com.listery.data.repository

import com.listery.data.model.UserShoppingList
import io.reactivex.Single

interface IShoppingListRepository {
    fun getAllShoppingLists(): Single<List<UserShoppingList>>
    fun getShoppingListEntity(name: String): Single<UserShoppingList>
}