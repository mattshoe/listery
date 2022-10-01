package com.listery.data.repository

import com.listery.data.model.UserShoppingList
import com.listery.data.repository.impl.DataStatus
import io.reactivex.Single

interface IShoppingListRepository: MutableRepository {
    fun getAllShoppingLists(): Single<List<UserShoppingList>>
    fun getShoppingList(name: String): Single<UserShoppingList>
}