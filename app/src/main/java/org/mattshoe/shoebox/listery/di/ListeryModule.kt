package org.mattshoe.shoebox.listery.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.util.ActivityProvider

@Module
@InstallIn(SingletonComponent::class)
interface ListeryModule {

    companion object {
        @Provides
        fun providesActivityProvider(): ActivityProvider = ActivityProvider

        @Provides
        fun providesFirestore(): FirebaseFirestore = Firebase.firestore
    }

}