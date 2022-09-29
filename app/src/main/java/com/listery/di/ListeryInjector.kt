package com.listery.di

import android.content.Context
import com.listery.ListeryApplication

object ListeryInjector {
    fun build(context: Context): ApplicationComponent {
        return DaggerApplicationComponent.builder().application(context.applicationContext as ListeryApplication).build()
    }
}