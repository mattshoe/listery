package org.mattshoe.shoebox.listery.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.util.ActivityProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ListeryModule {

    companion object {
        @Provides
        @Singleton
        fun providesActivityProvider(): ActivityProvider = ActivityProvider

        @Provides
        @Singleton
        fun providesFirestore(): FirebaseFirestore = Firebase.firestore

        @Provides
        @Singleton
        fun providesFirebaseStorage(): FirebaseStorage = Firebase.storage

    }

}