package com.listery.data.di

import android.app.Application
import androidx.room.Room
import com.listery.ListeryApplication
import com.listery.data.repository.IRecipeRepository
import com.listery.data.repository.RecipeRepository
import com.listery.data.room.AppDatabase
import com.listery.data.room.RecipeDao
import com.listery.data.room.RecipeMetadata
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
abstract class DataLayerModule {

    companion object {
        @Provides
        @Singleton
        fun providesRecipeDao(application: Application): RecipeDao {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                "recipe-database"
            ).build().also { db ->
                GlobalScope.launch {
                    val dao = db.recipeDao()
                    val recipes = mutableListOf<RecipeMetadata>().apply {
                        repeat(100) {
                            add(
                                RecipeMetadata(
                                    "Recipe #$it",
                                    "notes for recipe $it"
                                )
                            )
                        }
                    }
                    dao.insertRecipeMetadata(*recipes.toTypedArray())
                }
            }.recipeDao()
        }
    }

    @Singleton
    @Binds
    abstract fun bindsRecipeRepository(repository: RecipeRepository): IRecipeRepository

}