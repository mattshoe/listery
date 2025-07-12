package org.mattshoe.shoebox.listery.lifestyle.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.lifestyle.data.LifestyleRepository
import org.mattshoe.shoebox.listery.lifestyle.data.LifestyleRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LifestyleModule {
    @Binds
    @Singleton
    fun bindsCuisineRepository(impl: LifestyleRepositoryImpl): LifestyleRepository
}