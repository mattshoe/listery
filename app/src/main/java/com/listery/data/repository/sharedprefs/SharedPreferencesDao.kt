package com.listery.data.repository.sharedprefs

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesDao @Inject constructor(
    application: Application
) {
    companion object {
        private const val SHARED_PREFS_NAME = "com.listery.app"
    }

     val _sharedPreferences: SharedPreferences = application.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    inline fun <reified T> get(key: String): T? {
       return when (T::class.java) {
           String::class.java -> _sharedPreferences.getString(key, null)
           Long::class.java -> _sharedPreferences.getLong(key, 0L)
           Float::class.java -> _sharedPreferences.getFloat(key, 0F)
           Boolean::class.java -> _sharedPreferences.getBoolean(key, false)
           Set::class.java -> _sharedPreferences.getStringSet(key, emptySet())
           else -> throw RuntimeException("SharedPreferences does not support ${T::class.qualifiedName}")
       } as? T
    }

    inline fun <reified T> put(key: String, value: T) {
        when (T::class.java) {
            String::class.java -> _sharedPreferences.edit().putString(key, value as String)
            Long::class.java -> _sharedPreferences.edit().putLong(key, value as Long)
            Float::class.java -> _sharedPreferences.edit().putFloat(key, value as Float)
            Boolean::class.java -> _sharedPreferences.edit().putBoolean(key, value as Boolean)
            Set::class.java -> _sharedPreferences.edit().putStringSet(key, value as Set<String>)
            else -> throw RuntimeException("SharedPreferences does not support ${T::class.qualifiedName}")
        }
    }

}