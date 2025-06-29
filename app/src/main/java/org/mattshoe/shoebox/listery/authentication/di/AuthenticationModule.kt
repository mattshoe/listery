package org.mattshoe.shoebox.listery.authentication.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthenticationModule {
    companion object {

        @Provides
        fun providesFirebaseAuth(): FirebaseAuth {
            return Firebase.auth
        }

        @Provides
        fun providesCredentialManager(@ApplicationContext context: Context): CredentialManager {
            return CredentialManager.create(context)
        }
    }
}