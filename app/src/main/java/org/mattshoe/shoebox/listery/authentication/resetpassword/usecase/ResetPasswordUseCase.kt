package org.mattshoe.shoebox.listery.authentication.resetpassword.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.logd
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute(email: String) {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
        } catch (e: Throwable) {
            loge("Password reset failed", e)
        }
    }
}