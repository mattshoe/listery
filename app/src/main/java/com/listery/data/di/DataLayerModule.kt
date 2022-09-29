package com.listery.data.di

import android.app.Application
import com.listery.data.repository.IRecipeRepository
import com.listery.data.repository.RecipeRepository
import com.listery.data.room.DbInitializer
import com.listery.data.room.RecipeDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataLayerModule {

    companion object {
        @Provides
        @Singleton
        fun providesRecipeDao(application: Application): RecipeDao {
            return DbInitializer.initialize(application).recipeDao()
        }
    }

    @Singleton
    @Binds
    abstract fun bindsRecipeRepository(repository: RecipeRepository): IRecipeRepository

}