package org.mattshoe.shoebox.listery.cuisine.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.cuisine.data.CuisineRepository
import org.mattshoe.shoebox.listery.cuisine.data.CuisineRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CuisineModule {
    @Binds
    @Singleton
    fun bindsCuisineRepository(impl: CuisineRepositoryImpl): CuisineRepository
}