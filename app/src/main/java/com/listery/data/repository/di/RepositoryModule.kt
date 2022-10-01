package com.listery.data.repository.di

import android.app.Application
import com.listery.data.repository.IRecipeRepository
import com.listery.data.repository.IShoppingListRepository
import com.listery.data.repository.impl.RecipeRepository
import com.listery.data.repository.impl.ShoppingListRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule(
    application: Application
) {

    @Singleton
    @Binds
    abstract fun bindsRecipeRepository(repository: RecipeRepository): IRecipeRepository

    @Singleton
    @Binds
    abstract fun bindsShoppingListRepository(repository: ShoppingListRepository): IShoppingListRepository

}