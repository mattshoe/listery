package com.listery.di

import android.app.Application
import android.content.Context
import com.listery.ListeryApplication
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppModule {

    // could also be an abstract class
    @Singleton
    @Binds
    fun bindApplication(application: ListeryApplication): Application

    // if you also want to bind context
    @Singleton
    @Binds
    fun bindContext(application: ListeryApplication): Context
}