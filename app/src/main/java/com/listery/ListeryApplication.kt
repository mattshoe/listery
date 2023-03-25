package com.listery

import android.app.Application
import com.listery.data.room.AppDatabase
import com.listery.data.room.DbInitializer
import com.listery.di.ApplicationComponent
import com.listery.di.DaggerApplicationComponent

class ListeryApplication: Application() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder().application(this).build()
    }


}