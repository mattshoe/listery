package com.listery.di

import android.content.Context
import com.listery.ApplicationComponent
import com.listery.DaggerApplicationComponent
import com.listery.ListeryApplication

object AndroidInjector {
    fun build(context: Context): ApplicationComponent {
        return DaggerApplicationComponent.builder().application(context.applicationContext as ListeryApplication).build()
    }
}