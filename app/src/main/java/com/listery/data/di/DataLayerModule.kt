package com.listery.data.di

import android.app.Application
import com.listery.data.repository.IRecipeRepository
import com.listery.data.repository.IShoppingListRepository
import com.listery.data.repository.RecipeRepository
import com.listery.data.repository.ShoppingListRepository
import com.listery.data.room.AppDatabase
import com.listery.data.room.DbInitializer
import com.listery.data.room.RecipeDao
import com.listery.data.room.ShoppingListDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataLayerModule {

    companion object {

        @Provides
        @Singleton
        fun providesAppDatabase(application: Application): AppDatabase {
            return DbInitializer.initialize(application).apply {
                DbInitializer.seed(this)
            }
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

    @Singleton
    @Binds
    abstract fun bindsRecipeRepository(repository: RecipeRepository): IRecipeRepository

    @Singleton
    @Binds
    abstract fun bindsShoppingListRepository(repository: ShoppingListRepository): IShoppingListRepository

}