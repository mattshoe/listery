package org.mattshoe.shoebox.listery.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.data.firestore.RecipeFirestoreRepository
import org.mattshoe.shoebox.listery.data.RecipeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecipeModule {

    @Binds
    @Singleton
    fun bindRecipeRepository(impl: RecipeFirestoreRepository): RecipeRepository
}