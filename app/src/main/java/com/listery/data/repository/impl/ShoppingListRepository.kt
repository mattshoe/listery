package com.listery.data.repository.impl

import com.listery.data.model.UserShoppingList
import com.listery.data.observer.MutableDataObservable
import com.listery.data.repository.IShoppingListRepository
import com.listery.data.repository.sharedprefs.SharedPreferencesDao
import com.listery.data.room.ShoppingListDao
import com.listery.flatMapEach
import io.reactivex.Single
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListDao: ShoppingListDao,
    private val sharedPreferences: SharedPreferencesDao
) : IShoppingListRepository {

    companion object {
        private const val SELECTED_LIST_KEY = "selected_shopping_list"
        private const val DEFAULT_LIST = "Shopping"
    }

    override val onDataChanged = MutableDataObservable<DataStatus>()

    override fun getAllShoppingLists(): Single<List<UserShoppingList>> {
        return shoppingListDao.getAllShoppingListEntities()
            .flatMapEach { shoppingList ->
                getShoppingList(shoppingList.name)
            }
    }

    override fun getShoppingList(name: String): Single<UserShoppingList> {
        return shoppingListDao.getShopppingListEntity(name).flatMap { shoppingList ->
            shoppingListDao.getAllListItemEntities(shoppingList.name).map { items ->
                UserShoppingList(shoppingList, items)
            }
        }
    }

    private fun getCurrentList(): String {
        return sharedPreferences.get(SELECTED_LIST_KEY) ?: DEFAULT_LIST
    }
}

