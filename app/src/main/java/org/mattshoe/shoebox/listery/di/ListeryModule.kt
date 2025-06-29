package org.mattshoe.shoebox.listery.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.util.ActivityProvider

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    companion object {
        @Provides
        fun providesActivityProvider(): ActivityProvider = ActivityProvider
    }

}