package com.listery.data.room

import androidx.room.*
import com.listery.data.room.entities.shoppinglist.ListItemEntity
import com.listery.data.room.entities.shoppinglist.ShoppingListEntity
import io.reactivex.Single

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list")
    fun getAllShoppingListEntities(): Single<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_list WHERE name LIKE :name LIMIT 1")
    fun getShopppingListEntity(name: String): Single<ShoppingListEntity>

    @Query("SELECT * FROM list_item")
    fun getAllListItemEntities(): Single<List<ListItemEntity>>

    @Query("SELECT * FROM list_item WHERE owningList LIKE :shoppingList")
    fun getAllListItemEntities(shoppingList: String): Single<List<ListItemEntity>>

    @Query("SELECT * FROM list_item WHERE complete == false")
    fun getActiveListItemEntities(): Single<List<ListItemEntity>>

    @Query("SELECT * FROM list_item WHERE complete == true")
    fun getFinishedListItemEntities(): Single<List<ListItemEntity>>

    @Query("SELECT * FROM list_item WHERE owningList LIKE :shoppingList AND  complete == false")
    fun getActiveListItemEntities(shoppingList: String): Single<List<ListItemEntity>>

    @Query("SELECT * FROM list_item WHERE owningList LIKE :shoppingList AND  complete == true")
    fun getFinishedListItemEntities(shoppingList: String): Single<List<ListItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoppingListEntities(vararg item: ShoppingListEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListItemEntities(vararg item: ListItemEntity): List<Long>

    @Delete
    abstract fun delete(item: ShoppingListEntity): Int

    @Delete
    abstract fun delete(item: ListItemEntity): Int

    @Query("DELETE FROM list_item WHERE complete == true")
    abstract fun deleteFinishedListItemEntities(): Single<Int>

}