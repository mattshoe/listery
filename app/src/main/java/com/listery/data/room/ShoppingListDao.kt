package com.listery.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.listery.data.room.entities.shoppinglist.ListItem
import com.listery.data.room.entities.shoppinglist.ShoppingList

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shoppinglist")
    fun getShoppingLists(): LiveData<List<ShoppingList>>

    @Query("SELECT * FROM listitem")
    fun getListItems(): LiveData<List<ListItem>>

    @Query("SELECT * FROM listitem WHERE owningList LIKE :shoppingList")
    fun getListItems(shoppingList: String): LiveData<List<ListItem>>

    @Query("SELECT * FROM listitem WHERE complete == false")
    fun getActiveListItems(): LiveData<List<ListItem>>

    @Query("SELECT * FROM listitem WHERE complete == true")
    fun getFinishedListItems(): LiveData<List<ListItem>>

    @Query("SELECT * FROM listitem WHERE owningList LIKE :shoppingList AND  complete == false")
    fun getActiveListItems(shoppingList: String): LiveData<List<ListItem>>

    @Query("SELECT * FROM listitem WHERE owningList LIKE :shoppingList AND  complete == true")
    fun getFinishedListItems(shoppingList: String): LiveData<List<ListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoppingList(vararg item: ShoppingList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListItem(vararg item: ListItem)

    @Delete
    abstract fun delete(item: ShoppingList)

    @Delete
    abstract fun delete(item: ListItem)

}