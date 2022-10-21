package com.listery.data.room.di

import android.app.Application
import com.listery.data.room.AppDatabase
import com.listery.data.room.DbInitializer
import com.listery.data.room.RecipeDao
import com.listery.data.room.ShoppingListDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RoomModule {

    private lateinit var appDatabase: AppDatabase

    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        if (!this::appDatabase.isInitialized) {
            appDatabase = DbInitializer.initialize(application).apply {
//                DbInitializer.seed(this)
            }
        }

        return appDatabase
    }

    @Provides
    @Singleton
    fun providesRecipeDao(appDatabase: AppDatabase): RecipeDao {
        return appDatabase.recipeDao()
    }

    @Provides
    @Singleton
    fun providesShoppingListDao(appDatabase: AppDatabase): ShoppingListDao {
        return appDatabase.shoppingListDao()
    }

}