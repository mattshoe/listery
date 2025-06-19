package org.mattshoe.shoebox.listery.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.data.db.DatabaseSeeder
import org.mattshoe.shoebox.listery.data.db.ListeryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabaseSeeder(
        @ApplicationContext context: Context
    ): DatabaseSeeder {
        return DatabaseSeeder(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        seeder: DatabaseSeeder
    ): ListeryDatabase {
        return seeder.createInitialDatabase()
    }

    @Provides
    fun provideRecipeDao(database: ListeryDatabase) = database.recipeDao()

    @Provides
    fun provideIngredientDao(database: ListeryDatabase) = database.ingredientDao()

    @Provides
    fun provideRecipeIngredientDao(database: ListeryDatabase) = database.recipeIngredientDao()

    @Provides
    fun provideRecipeStepDao(database: ListeryDatabase) = database.recipeStepDao()
} 