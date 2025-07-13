package org.mattshoe.shoebox.listery.authentication.profile.usecase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute() {
        firebaseAuth.signOut()
    }
} 