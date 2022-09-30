package com.listery.data.repository

import com.listery.data.model.UserShoppingList
import com.listery.data.room.ShoppingListDao
import com.listery.data.room.entities.shoppinglist.ShoppingListEntity
import com.listery.flatMapEach
import io.reactivex.Single
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListDao: ShoppingListDao
) : IShoppingListRepository {

    override fun getAllShoppingLists(): Single<List<UserShoppingList>> {
        return shoppingListDao.getAllShoppingListEntities()
            .flatMapEach { shoppingList ->
                getShoppingListEntity(shoppingList.name)
            }
    }

    override fun getShoppingListEntity(name: String): Single<UserShoppingList> {
        return shoppingListDao.getShopppingListEntity(name).flatMap { shoppingList ->
            shoppingListDao.getAllListItemEntities(shoppingList.name).map { items ->
                UserShoppingList(shoppingList, items)
            }
        }
    }
}

